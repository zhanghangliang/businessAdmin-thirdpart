/**
 * @Title: QuestionAuditServiceImpl.java
 * @Package com.gov.wiki.business.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.wiki.business.dao.BizApprovalRecordDao;
import com.gov.wiki.business.dao.BizQuestionAuditDao;
import com.gov.wiki.business.dao.BizQuestionDao;
import com.gov.wiki.business.dao.BizQuestionOpinionAuditDao;
import com.gov.wiki.business.enums.OperTypeEnum;
import com.gov.wiki.business.enums.StatusEnum;
import com.gov.wiki.business.req.OpinionMaterialAuditReq;
import com.gov.wiki.business.req.QuestionAuditAuditReq;
import com.gov.wiki.business.req.QuestionAuditReq;
import com.gov.wiki.business.req.QuestionOpinionAuditReq;
import com.gov.wiki.business.req.query.QuestionQuery;
import com.gov.wiki.business.service.IQuestionAuditService;
import com.gov.wiki.business.service.IQuestionService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizApprovalRecord;
import com.gov.wiki.common.entity.buss.BizOpinionMaterialAudit;
import com.gov.wiki.common.entity.buss.BizQuestion;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.entity.buss.BizQuestionOpinionAudit;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ObjectMapperUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: QuestionAuditServiceImpl
 * @Description: 问题审批管理业务处理接口实现
 * @author cys
 * @date 2020年8月25日
 */
@Service
@Slf4j
public class QuestionAuditServiceImpl extends BaseServiceImpl<BizQuestionAudit, String, BizQuestionAuditDao> implements IQuestionAuditService {

	@Autowired
	private BizQuestionOpinionAuditDao questionOpinionAuditDao;
	@Autowired
	private BizApprovalRecordDao approvalRecordDao;
	
	@Autowired
	private IQuestionService questionService;
	
	@Autowired
	private BizQuestionDao bizQuestionDao;
	
	@Override
	public BizQuestionAudit saveOrUpdate(QuestionAuditReq req) {
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
		CheckClass.check(req);
		req.setStatus(StatusEnum.save.getValue());
		OperTypeEnum ote = OperTypeEnum.getEnumByKey(req.getOperType());
		CheckUtil.notNull(ote, ResultCode.COMMON_ERROR, "操作类型不存在！");
		List<QuestionOpinionAuditReq> opinionList = req.getOpinionList();
		CheckUtil.notNull(opinionList, ResultCode.COMMON_ERROR, "题目选项不存在！");
		int sortNo = 0;
		for(QuestionOpinionAuditReq opReq:opinionList) {
			CheckUtil.notNull(opReq, ResultCode.COMMON_ERROR, "选项参数不存在！");
			CheckClass.check(opReq);
			BizQuestionOpinionAudit op = opReq.toEntity();
			op.setSortNo(sortNo);
			sortNo++;
			List<OpinionMaterialAuditReq> materialList = opReq.getMaterialList();
			if(materialList != null && !materialList.isEmpty()) {
				Set<BizOpinionMaterialAudit> maSet = new HashSet<BizOpinionMaterialAudit>();
				for(OpinionMaterialAuditReq maReq:materialList) {
					if(maReq == null) {
						continue;
					}
					CheckClass.check(maReq);
					BizOpinionMaterialAudit ma = maReq.toEntity();
					maSet.add(ma);
				}
			}
		}
		BizQuestionAudit question = BeanUtils.copyProperties(req, BizQuestionAudit.class);
		BizQuestionAudit old = null;
		if(StringUtils.isNotBlank(req.getId())) {
			old = this.findById(req.getId());
		}
		int modifyTimes = 1;
		if(old != null) {
			question.setVersion(old.getVersion());
			question.setModifyTimes(old.getModifyTimes());
		}else {
			if(StringUtils.isNotBlank(question.getReleaseId())) {
				BizQuestion q = questionService.findById(question.getReleaseId());
				if(q != null && q.getModifyTimes() != null) {
					modifyTimes += q.getModifyTimes();
				}
			}
			question.setModifyTimes(modifyTimes);
			question.setVersion("V" + modifyTimes + ".0");
		}
		question = this.saveOrUpdate(question);
		return question;
	}
	
	@Override
	public BizQuestionAudit saveOrUpdate(BizQuestionAudit t) {
		if(StringUtils.isNotBlank(t.getId())) {
			//修改
			BizQuestionAudit old = this.findById(t.getId());
			if(old == null) {
				addAduit(t);
			} else {
				updateAudit(old,t);
			}
		} else {
			addAduit(t);
		}
		return t;
	}

	private void updateAudit(BizQuestionAudit old, BizQuestionAudit t) {
		List<BizQuestionOpinionAudit> oldOpinions = old.getOpinionList();
		List<BizQuestionOpinionAudit> opinions = t.getOpinionList();
		this.baseRepository.save(t);
		if(opinions != null && !opinions.isEmpty()) {
			for (BizQuestionOpinionAudit o : opinions) {
				if(oldOpinions.contains(o)) {
					oldOpinions.remove(o);
				}
			}
			this.questionOpinionAuditDao.saveAll(opinions);
		}
		if(!oldOpinions.isEmpty()) {
			for (BizQuestionOpinionAudit o : oldOpinions) {
				if(o == null)continue;
				log.info("id：{}",o.getId());
				this.questionOpinionAuditDao.deleteById(o.getId());
			}
		} 
	}

	private void addAduit(BizQuestionAudit t) {
		List<BizQuestionOpinionAudit> opinions = t.getOpinionList();
		t = this.baseRepository.saveAndFlush(t);
		log.info("数据:{}",ObjectMapperUtil.objToStr(t));
		if(opinions != null && !opinions.isEmpty()) {
			for (BizQuestionOpinionAudit o : opinions) {
				
				if(o == null) continue;
				o.setQuestionAuditId(t.getId());
			}
			this.questionOpinionAuditDao.saveAll(opinions);
		}
	}

	@Override
	public ResultBean<PageInfo> pageList(ReqBean<QuestionQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final QuestionQuery param = bean.getBody() == null?new QuestionQuery():bean.getBody();
		Specification<BizQuestionAudit> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("name").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("description").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("version").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			if(param.getOperTypeList() != null) {
				Expression<Integer> expOp = root.<Integer>get("operType");
				predicate.getExpressions().add(expOp.in(param.getOperTypeList()));
			}
			if(param.getStatusList() != null && !param.getStatusList().isEmpty()) {
				Expression<Integer> exp = root.<Integer>get("status");
				predicate.getExpressions().add(exp.in(param.getStatusList()));
			}
            return predicate;
        };
 
		Page<BizQuestionAudit> page = this.baseRepository.findAll(spec, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public void audit(QuestionAuditAuditReq req) {
		String userId = JwtUtil.getUserId();
		BizQuestionAudit audit = this.findById(req.getId());
		CheckUtil.notNull(audit, ResultCode.DATA_NOT_EXIST, "审核的问题");
		BizApprovalRecord record = new BizApprovalRecord();
		record.setApprovalTime(new Date());
		record.setApprover(userId);
		record.setObjectId(req.getId());
		record.setOpinion(req.getOpinion());
		if(req.getStatus() == StatusEnum.agree.getValue()) {
			if(audit.getOperType() == OperTypeEnum.ADD.getKey() 
					|| audit.getOperType() == OperTypeEnum.MODIFY.getKey()) {
				BizQuestion question = audit.toQuestion();
				if(StringUtils.isNotBlank(audit.getReleaseId())) {
					bizQuestionDao.recoveryQuestion(audit.getReleaseId());
					audit.setHisReleaseId(audit.getReleaseId());
				}
				question = this.questionService.saveOrUpdate(question);
				audit.setReleaseId(question.getId());
			} else if(audit.getOperType() == OperTypeEnum.DELETE.getKey()) {
				if(StringUtils.isNotBlank(audit.getReleaseId())) {
					this.questionService.delById(audit.getReleaseId());
				}
			}
			audit.setStatus(StatusEnum.agree.getValue());
			record.setResult(StatusEnum.agree.getValue());
		} else if(req.getStatus() == StatusEnum.disagree.getValue()) {
			audit.setStatus(StatusEnum.disagree.getValue());
			record.setResult(StatusEnum.disagree.getValue());
		} else {
			CheckUtil.check(false, ResultCode.COMMON_ERROR, "审核状态超出范围");
		}
		this.baseRepository.save(audit);
		this.approvalRecordDao.save(record);
	}
}