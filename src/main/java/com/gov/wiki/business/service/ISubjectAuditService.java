package com.gov.wiki.business.service;

import com.gov.wiki.business.res.SubjectAuditBasicRes;
import com.gov.wiki.common.res.PageResult;
import org.springframework.data.domain.Page;

import com.gov.wiki.business.req.AuditReq;
import com.gov.wiki.business.req.SubjectAuditReq;
import com.gov.wiki.business.req.query.SubjectQuery;
import com.gov.wiki.common.entity.buss.BizSubjectAudit;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;

/**
 * @ClassName: ISubjectAuditService
 * @Description: 主题审批管理业务处理接口
 * @author cys
 * @date 2020年8月25日
 */
public interface ISubjectAuditService extends IBaseService<BizSubjectAudit, String> {

	/**
	 * 创建或修改主题
	 * @param body
	 * @return
	 */
	BizSubjectAudit saveOrUpdate(SubjectAuditReq body);

	void audit(AuditReq body);

	Page<BizSubjectAudit> page(ReqBean<SubjectQuery> bean);

	PageResult<SubjectAuditBasicRes> pageBasic(ReqBean<SubjectQuery> bean);
	
}
