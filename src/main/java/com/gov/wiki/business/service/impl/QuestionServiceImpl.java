/**
 * @Title: QuestionServiceImpl.java
 * @Package com.gov.wiki.business.service.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizQuestionAuditDao;
import com.gov.wiki.business.dao.BizQuestionDao;
import com.gov.wiki.business.dao.BizQuestionOpinionDao;
import com.gov.wiki.business.enums.OperTypeEnum;
import com.gov.wiki.business.enums.StatusEnum;
import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.query.QuestionQuery;
import com.gov.wiki.business.service.IQuestionService;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.BizQuestion;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.entity.buss.BizQuestionOpinion;
import com.gov.wiki.common.entity.buss.BizQuestionOpinionAudit;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: QuestionServiceImpl
 * @Description: 问题管理业务处理接口实现
 * @author cys
 * @date 2020年8月25日
 */
@Service
@Slf4j
public class QuestionServiceImpl extends BaseServiceImpl<BizQuestion, String, BizQuestionDao> implements IQuestionService {

	@Autowired
	private BizQuestionAuditDao questionAuditDao;
	@Autowired
	private BizQuestionOpinionDao questionOpinionDao;
	
	@Override
	public Page<BizQuestion> page(ReqBean<QuestionQuery> bean) {
		QuestionQuery query = bean.getBody();
		if(query == null) query = new QuestionQuery();
		PredicateBuilder<BizQuestion> builder = Specifications.and();
		if(StringUtils.isNotBlank(query.getKeywords())) {
			builder.predicate(Specifications.or()
				.like("name", "%" + query.getKeywords() + "%")
				.like("version", "%" + query.getKeywords() + "%")
				.build());
		}
		if(query.getRecyclingMark() != null) {
			builder.eq("recyclingMark", query.getRecyclingMark());
		}
		return this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
	}

	@Override
	public void delQuestions(DataDelReq req) {
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在！");
    	List<String> ids = req.getDelIds();
    	CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "待删除记录不存在！");
		List<BizQuestion> qList = this.findByIds(ids);
		CheckUtil.check(qList != null && !qList.isEmpty(), ResultCode.COMMON_ERROR, "待删除问题库列表信息不存在");
		List<BizQuestionAudit> auditList = new ArrayList<BizQuestionAudit>();
		for(BizQuestion question:qList) {
			CheckUtil.notNull(question, ResultCode.COMMON_ERROR, "待删除问题信息不存在");
			BizQuestionAudit audit = question.toAudit();
			audit.setOperType(OperTypeEnum.DELETE.getKey());
			audit.setReleaseId(question.getId());
			audit.setOperReason(req.getReason());
			audit.setStatus(StatusEnum.save.getValue());
			auditList.add(audit);
		}
		
		if(!auditList.isEmpty()) {
			questionAuditDao.saveAll(auditList);
		}
	}

	@Override
	public void saveOrUpdate(BizQuestionAudit audit) {
		BizQuestion question = BeanUtils.copyProperties(audit, BizQuestion.class);
		if(audit.getReleaseId() != null) {
			question.setId(audit.getReleaseId());
		}
		question.toChangeOpinions(audit.getOpinionList());
		this.saveOrUpdate(question);
	}

	@Override
	public BizQuestion saveOrUpdate(BizQuestion t) {
		if(StringUtils.isNotBlank(t.getId())) {
			//修改
			BizQuestion old = this.findById(t.getId());
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
	
	private void updateAudit(BizQuestion old, BizQuestion t) {
		// TODO Auto-generated method stub
		List<BizQuestionOpinion> oldOpinions = old.getOpinionList();
		List<BizQuestionOpinion> opinions = t.getOpinionList();
		this.baseRepository.save(t);
		if(opinions != null && !opinions.isEmpty()) {
			for (BizQuestionOpinion o : opinions) {
				if(oldOpinions.contains(o)) {
					oldOpinions.remove(o);
				}
			}
			this.questionOpinionDao.saveAll(opinions);
		}
		if(!oldOpinions.isEmpty()) {
			for (BizQuestionOpinion o : oldOpinions) {
				if(o == null)continue;
				log.info("id：{}",o.getId());
				this.baseRepository.deleteById(o.getId());
			}
		} 
	}

	private void addAduit(BizQuestion t) {
		List<BizQuestionOpinion> opinions = t.getOpinionList();
		t = this.baseRepository.saveAndFlush(t);
		if(opinions != null && !opinions.isEmpty()) {
			for (BizQuestionOpinion o : opinions) {
				o.setQuestionId(t.getId());
			}
			this.questionOpinionDao.saveAll(opinions);
		}
	}

	@Override
	public void delById(String releaseId) {
		super.deleteById(releaseId);
	}
}