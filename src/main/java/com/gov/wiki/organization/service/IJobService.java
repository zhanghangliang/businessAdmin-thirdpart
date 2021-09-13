/**
 * @Title: IJobService.java
 * @Package com.gov.wiki.organization.service
 * @Description: 职务管理处理接口
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import java.util.List;

import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.BaseQuery;

public interface IJobService extends IBaseService<OrgJob, String> {

	/**
	 * @Title: countJob 
	 * @Description: 统计职务级别数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	int countJob();
	
	/**
	 * @Title: queryJobList 
	 * @Description: 根据参数查询职务列表信息
	 * @param 设定文件 
	 * @return List<OrgJob>    返回类型 
	 * @throws
	 */
	List<OrgJob> queryJobList(ReqBean<BaseQuery> bean);
}
