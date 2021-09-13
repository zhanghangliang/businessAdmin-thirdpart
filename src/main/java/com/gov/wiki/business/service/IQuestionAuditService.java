/**
 * @Title: IQuestionAuditService.java
 * @Package com.gov.wiki.business.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.service;

import org.springframework.data.domain.Page;

import com.gov.wiki.business.req.QuestionAuditAuditReq;
import com.gov.wiki.business.req.QuestionAuditReq;
import com.gov.wiki.business.req.query.QuestionQuery;
import com.gov.wiki.business.res.QuestionRes;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;

/**
 * @ClassName: IQuestionAuditService
 * @Description: 问题审批管理业务处理接口
 * @author cys
 * @date 2020年8月25日
 */
public interface IQuestionAuditService extends IBaseService<BizQuestionAudit, String>{

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改问题信息
	 * @param req
	 * @return ResultBean<BizQuestionAudit> 返回类型
	 * @throws
	 */
	BizQuestionAudit saveOrUpdate(QuestionAuditReq req);
	
	/**
	 * @Title: pageList
	 * @Description: 分页查询问题审批记录信息
	 * @param bean
	 * @return ResultBean<PageInfo> 返回类型
	 * @throws
	 */
	ResultBean<PageInfo> pageList(ReqBean<QuestionQuery> bean);

	/**
	 * 审核问题库
	 * @param questionAuditAuditReq
	 */
	void audit(QuestionAuditAuditReq questionAuditAuditReq);
}