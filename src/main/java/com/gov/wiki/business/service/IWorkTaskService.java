/**
 * @Title: IWorkTaskService.java
 * @Package com.gov.wiki.business.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizWorkTask;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.BaseQuery;

/**
 * @ClassName: IWorkTaskService
 * @Description: 工作任务管理业务处理接口
 * @author cys
 * @date 2020年11月25日
 */
public interface IWorkTaskService extends IBaseService<BizWorkTask, String> {

	/**
	 * @Title: pageList
	 * @Description: 分页查询工作任务信息
	 * @param bean
	 * @return PageInfo 返回类型
	 * @throws
	 */
	PageInfo pageList(ReqBean<BaseQuery> bean);
}
