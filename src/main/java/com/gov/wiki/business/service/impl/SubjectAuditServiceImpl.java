package com.gov.wiki.business.service.impl;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizApprovalRecordDao;
import com.gov.wiki.business.dao.BizSubjectAuditDao;
import com.gov.wiki.business.dao.BizSubjectDao;
import com.gov.wiki.business.enums.OperTypeEnum;
import com.gov.wiki.business.enums.StatusEnum;
import com.gov.wiki.business.req.AuditReq;
import com.gov.wiki.business.req.SubjectAuditReq;
import com.gov.wiki.business.req.query.SubjectQuery;
import com.gov.wiki.business.res.SubjectAuditBasicRes;
import com.gov.wiki.business.service.ISubjectAuditService;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizApprovalRecord;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.buss.BizSubjectAudit;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.system.dao.SysFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cys
 * @ClassName: SubjectAuditServiceImpl
 * @Description: 主题审批管理业务处理接口实现
 * @date 2020年8月25日
 */
@Service("subjectAuditService")
public class SubjectAuditServiceImpl extends BaseServiceImpl<BizSubjectAudit, String, BizSubjectAuditDao> implements ISubjectAuditService {

    @Autowired
    private BizSubjectDao subjectDao;
    @Autowired
    private BizApprovalRecordDao approvalRecordDao;
    @Autowired
    private SysFileDao fileDao;
    @Autowired
    private ISubjectService subjectService;

    @Override
    public BizSubjectAudit saveOrUpdate(SubjectAuditReq req) {
        CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
        CheckClass.check(req);
        BizSubjectAudit bean = BeanUtils.copyProperties(req, BizSubjectAudit.class);
        BizSubjectAudit old = null;
        if (StringUtils.isNotBlank(req.getId())) {
            old = super.findById(req.getId());
        }
        if (old != null) {
            bean.setVersion(old.getVersion());
            bean.setModifyTimes(old.getModifyTimes());
            bean.setOperType(old.getOperType());
            bean.setReleaseId(old.getReleaseId());
        } else {
            String releaseId = bean.getReleaseId();
            BizSubject subject = null;
            int modifyTimes = 1;
            if (StringUtils.isNotBlank(releaseId)) {
                subject = subjectService.findById(releaseId);
            }
            if (subject != null) {
                bean.setOperType(OperTypeEnum.MODIFY.getKey());
                modifyTimes += (subject.getModifyTimes() == null ? 0 : subject.getModifyTimes().intValue());
            } else {
                bean.setOperType(OperTypeEnum.ADD.getKey());
                bean.setReleaseId(null);
            }
            bean.setModifyTimes(modifyTimes);
            bean.setVersion("V" + modifyTimes + ".0");
        }
        bean.setStatus(StatusEnum.save.getValue());
        return this.saveOrUpdate(bean);
    }

    @Override
    public BizSubjectAudit saveOrUpdate(BizSubjectAudit t) {
		/*if(t.getId() != null) {
			BizSubjectAudit audit = this.findById(t.getId());
			CheckUtil.notNull(audit, ResultCode.DATA_NOT_EXIST, "主题审核信息");
			audit.merge(t);
		} */
        return super.saveOrUpdate(t);
    }

    @Override
    @Transactional
    public void audit(AuditReq req) {
        CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
        CheckClass.check(req);
        String userId = JwtUtil.getUserId();
        CheckUtil.notEmpty(userId, ResultCode.COMMON_ERROR, "用户未登录！");
        CheckUtil.check(checkStatus(req.getId(),StatusEnum.save),ResultCode.COMMON_ERROR ,"该项目已经被审核了" );
        BizSubjectAudit audit = this.findById(req.getId());
        CheckUtil.notNull(audit, ResultCode.DATA_NOT_EXIST, "待审核的主题");
        BizApprovalRecord record = new BizApprovalRecord();
        record.setApprovalTime(new Date());
        record.setApprover(userId);
        record.setObjectId(req.getId());
        record.setOpinion(req.getOpinion());
        if (req.getStatus() == StatusEnum.agree.getValue()) {
            BizSubject subject = audit.toSubject();
            if (audit.getOperType() == OperTypeEnum.ADD.getKey()
                    || audit.getOperType() == OperTypeEnum.MODIFY.getKey()) {
                if (StringUtils.isNotBlank(audit.getReleaseId())) {// 历史主题数据进入回收站
                    subjectDao.recoverySubject(audit.getReleaseId());
                    audit.setHisReleaseId(audit.getReleaseId());
                }
                subject = subjectDao.saveAndFlush(subject);
                audit.setReleaseId(subject.getId());
            } else if (audit.getOperType() == OperTypeEnum.DELETE.getKey()) {
                if (StringUtils.isNotBlank(audit.getReleaseId())) {
                    if (this.subjectDao.findById(audit.getReleaseId()).isPresent()) {
                        this.subjectDao.deleteById(audit.getReleaseId());
                    }
                }
            }
            audit.setStatus(StatusEnum.agree.getValue());
            record.setResult(StatusEnum.agree.getValue());
        } else if (req.getStatus() == StatusEnum.disagree.getValue()) {
            audit.setStatus(StatusEnum.disagree.getValue());
            record.setResult(StatusEnum.disagree.getValue());
        } else {
            CheckUtil.check(false, ResultCode.COMMON_ERROR, "审核状态超出范围");
        }
        this.baseRepository.save(audit);
        this.approvalRecordDao.save(record);
    }

    /**
     * 检查审核项目是否存在，并检查审核状态是否是Enum
     * @param subid
     * @param statusEnum
     * @return
     */
    private boolean checkStatus(String subid,StatusEnum statusEnum){
        PredicateBuilder<BizSubjectAudit> builder = Specifications.and();
        builder.predicate((root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            Selection<Integer> status= root.get("status");
            Selection<String> id= root.get("id");
            criteriaQuery.multiselect(id,status);
            return predicate;
        });
        builder.eq("id",subid);
        List<Object[]> result = this.listSelect(builder.build(), BizSubjectAudit.class);
        if(!CollectionUtils.isEmpty(result) && result.size()>0){
            return (Integer) result.get(0)[1] == statusEnum.getValue();
        }else {
            CheckUtil.check(false,ResultCode.DATA_NOT_EXIST, "待审核的主题");
        }
        return false;
    }

    @Override
    public Page<BizSubjectAudit> page(ReqBean<SubjectQuery> bean) {
        PredicateBuilder<BizSubjectAudit> builder = createBuilder(bean);
        return this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
    }

    @Override
    public PageResult<SubjectAuditBasicRes> pageBasic(ReqBean<SubjectQuery> bean) {
        PredicateBuilder<BizSubjectAudit> builder = createBuilder(bean);
        Page<BizSubjectAudit> bizSubjectAudits = this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
        return BeanUtils.pageCopy(bizSubjectAudits, SubjectAuditBasicRes.class);
    }

    private PredicateBuilder<BizSubjectAudit> createBuilder(ReqBean<SubjectQuery> bean) {
        SubjectQuery query = bean.getBody() == null ? new SubjectQuery() : bean.getBody();
        PredicateBuilder<BizSubjectAudit> builder = Specifications.and();
        if (StringUtils.isNotBlank(query.getKeywords())) {
            builder.predicate(Specifications.or()
                    .like("name", "%" + query.getKeywords() + "%")
                    .like("keyDescription", "%" + query.getKeywords() + "%")
                    .like("version", "%" + query.getKeywords() + "%")
                    .build());
        }
        if (query.getOperTypeList() != null && !query.getOperTypeList().isEmpty()) {
            builder.in("operType", query.getOperTypeList());
        }
        if (query.getStatusList() != null && !query.getStatusList().isEmpty()) {
            builder.in("status", query.getStatusList());
        }
        if (StringUtils.isNotBlank(query.getSubjectType())) {
            builder.eq("subjectType", query.getSubjectType());
        }
        if (query.getMajorCategorys() != null && !query.getMajorCategorys().isEmpty()) {
            builder.in("majorCategory", query.getMajorCategorys());
        }
        return builder;
    }

    @Override
    public BizSubjectAudit findById(String id) {
        BizSubjectAudit audit = super.findById(id);
        if (audit != null) {
            if (audit.getProcessFlow() != null) {
                audit.setSysFiles(fileDao.findByReferenceId(audit.getProcessFlow()));
            }
        }
        return audit;
    }
}