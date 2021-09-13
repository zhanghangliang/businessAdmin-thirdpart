/**
 * @Title: IQuestionService.java
 * @Package com.gov.wiki.business.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.service;

import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.query.QuestionQuery;
import com.gov.wiki.common.entity.buss.BizQuestion;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;

/**
 * @ClassName: IQuestionService
 * @Description: 问题管理业务处理接口
 * @author cys
 * @date 2020年8月25日
 */
public interface IQuestionService extends IBaseService<BizQuestion, String>{

	Page<BizQuestion> page(ReqBean<QuestionQuery> bean);

	void saveOrUpdate(BizQuestionAudit audit);

	void delById(String releaseId);

	@Transactional
	void delQuestions(DataDelReq req);
}