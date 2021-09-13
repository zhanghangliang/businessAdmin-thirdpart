package com.gov.wiki.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizQuestionOpinionDao;
import com.gov.wiki.business.dao.BizSubjectAuditDao;
import com.gov.wiki.business.dao.BizSubjectDao;
import com.gov.wiki.business.dao.BizSubjectQaRelationshipDao;
import com.gov.wiki.business.enums.OperTypeEnum;
import com.gov.wiki.business.enums.StatusEnum;
import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.query.SubjectQuery;
import com.gov.wiki.business.res.SubjectWxRes;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.buss.vo.SimpleSubjectRes;
import com.gov.wiki.common.entity.buss.BizOpinionMaterial;
import com.gov.wiki.common.entity.buss.BizQuestionOpinion;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.buss.BizSubjectAudit;
import com.gov.wiki.common.entity.buss.BizSubjectQaRelationship;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.dao.SysFileDao;
import com.gov.wiki.wechat.req.OperationReq;

/**
 * @ClassName: SubjectServiceImpl
 * @Description: 主题管理业务处理接口实现
 * @author cys
 * @date 2020年8月25日
 */
@Service("subjectService")
public class SubjectServiceImpl extends BaseServiceImpl<BizSubject, String, BizSubjectDao> implements ISubjectService {

	@Autowired
	private BizSubjectAuditDao subjectAuditDao;
	@Autowired
	private BizSubjectQaRelationshipDao subjectQaRelationshipDao;
	@Autowired
	private BizQuestionOpinionDao questionOpinionDao;
	@Autowired
	private SysFileDao fileDao;
	
	@Override
	public Page<BizSubject> page(ReqBean<SubjectQuery> bean) {
		SubjectQuery query = bean.getBody();
		PredicateBuilder<BizSubject> builder = createBuilder(query);
		return this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
	}

	private PredicateBuilder<BizSubject> createBuilder(SubjectQuery query) {
		if(query == null) {
			query = new SubjectQuery();
		}
		PredicateBuilder<BizSubject> builder = Specifications.and();
		if(StringUtils.isNotBlank(query.getKeywords())) {
			builder.predicate(Specifications.or()
				.like("name", "%" + query.getKeywords() + "%")
				.like("keyDescription", "%" + query.getKeywords() + "%")
				.like("version", "%" + query.getKeywords() + "%")
				.build());
		}
		if(StringUtils.isNotBlank(query.getSubjectType())) {
			builder.eq("subjectType", query.getSubjectType());
		}
		if(query.getRecyclingMark() != null) {
			builder.eq("recyclingMark", query.getRecyclingMark());
		}
		if(query.getMajorCategorys() != null && !query.getMajorCategorys().isEmpty()) {
			builder.in("majorCategory", query.getMajorCategorys());
		}
		return builder;
	}

	@Override
	public void delSubject(DataDelReq req) {
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
    	List<String> ids = req.getDelIds();
    	CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "待删除记录不存在！");
		List<BizSubject> subjectList = this.findByIds(ids);
		CheckUtil.check(subjectList != null && !subjectList.isEmpty(), ResultCode.COMMON_ERROR, "待删除主题库列表信息不存在");
		List<BizSubjectAudit> auditList = new ArrayList<BizSubjectAudit>();
		for(BizSubject subject:subjectList) {
			CheckUtil.notNull(subject, ResultCode.COMMON_ERROR, "待删除主题信息不存在");
			BizSubjectAudit audit = subject.toAudit();
			audit.setOperType(OperTypeEnum.DELETE.getKey());
			audit.setReleaseId(subject.getId());
			audit.setOperReason(req.getReason());
			audit.setStatus(StatusEnum.save.getValue());
			auditList.add(audit);
		}
		
		if(!auditList.isEmpty()) {
			subjectAuditDao.saveAll(auditList);
		}
	}

	@Override
	public List<BizOpinionMaterial> findMaterial(OperationReq body) {
		List<BizOpinionMaterial> materials = new ArrayList<BizOpinionMaterial>();
		for (String b : body.getOpinionIds()) {
			Optional<BizQuestionOpinion> op = this.questionOpinionDao.findById(b);
			if(op.isPresent()) {
				materials.addAll(op.get().getMaterialList());
			}
		}
		BizSubject subject = this.findById(body.getSubjectId());
		CheckUtil.notNull(subject, ResultCode.DATA_NOT_EXIST, "主题信息");
		List<BizOpinionMaterial> list = BeanUtils.listCopy(subject.getMaterials(), BizOpinionMaterial.class);
		materials.addAll(list);
		return materials;
	}

	private void addMaterial(BizSubjectQaRelationship s, List<BizOpinionMaterial> materials) {
		if(s == null) return;
		BizQuestionOpinion opinion = s.getOpinion();
		if(opinion != null) {
			materials.addAll(opinion.getMaterialList());
		}
		if(s.getSubjectId() != null) {
			BizSubject subject  = this.findById(s.getSubjectId());
			if(subject != null) {
				List<BizOpinionMaterial> list = BeanUtils.listCopy(subject.getMaterials(), BizOpinionMaterial.class);
				materials.addAll(list);
			}
		}
		if(s.getPreRelationshipId() != null) {
			Optional<BizSubjectQaRelationship> ship = this.subjectQaRelationshipDao.findById(s.getPreRelationshipId());
			if(ship.isPresent()) {
				this.addMaterial(ship.get(), materials);
			}
		}
	}

	@Override
	public List<BizSubjectQaRelationship> findRelationShip(String lastQaRelationShipId) {
		Optional<BizSubjectQaRelationship> optional = this.subjectQaRelationshipDao.findById(lastQaRelationShipId);
		CheckUtil.check(optional.isPresent(), ResultCode.DATA_NOT_EXIST, "问题关系");
		return BeanUtils.listCopy(optional.get().getPreRelationships(), BizSubjectQaRelationship.class);
	}

	@Override
	public BizSubject findById(String id) {
		BizSubject subject = super.findById(id);
		if(subject != null) {
			if(subject.getProcessFlow() != null) {
				subject.setSysFiles(fileDao.findByReferenceId(subject.getProcessFlow()));
			}
		}
		return subject;
	}

	@Override
	public SimpleSubjectRes findSimpleById(String id) {
		SimpleSubjectRes res = null;
		if(StringUtils.isNotBlank(id)) {
			BizSubject subject = this.findById(id);
			res = BeanUtils.copyProperties(subject, SimpleSubjectRes.class);
		}
		return res;
	}

	@Override
	public PageResult<SubjectWxRes> wxPage(ReqBean<SubjectQuery> bean) {
		PredicateBuilder<BizSubject> builder = createBuilder(bean.getBody());
		builder.predicate((root,query,cb) -> {
            Predicate predicate = cb.conjunction();

            Selection<String> id = root.get("id");
            Selection<String> name = root.get("name");

            query.multiselect(id, name);
            return predicate;
		});
		PageResult<SubjectWxRes> res = new PageResult<SubjectWxRes>();
		
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
        Root<BizSubject> root = query.from(BizSubject.class);
        Predicate predicate = builder.build().toPredicate(root, query, cb);
        if (predicate != null) {
            query.where(predicate);
        }
        TypedQuery<Tuple> tQuery = entityManager.createQuery(query);
        List<SubjectWxRes> list = new ArrayList<SubjectWxRes>();
        List<Tuple> result = tQuery.getResultList();
        for (Tuple o : result) {
        	SubjectWxRes r = new SubjectWxRes();
            r.setId(o.get(0, String.class));
            r.setName(o.get(1, String.class));
            list.add(r);
        }
        res.setDataList(list);
        return res;
	}
}