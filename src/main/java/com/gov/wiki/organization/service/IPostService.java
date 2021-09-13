/**
 * @Title: IPostService.java
 * @Package com.gov.wiki.organization.service
 * @Description: 岗位管理处理接口
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import java.util.List;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.BaseQuery;

public interface IPostService extends IBaseService<OrgPost, String> {

	/**
	 * @Title: countPost 
	 * @Description: 统计岗位数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	int countPost();
	
	/**
	 * @Title: queryPostList 
	 * @Description: 根据参数查询岗位列表
	 * @param 设定文件 
	 * @return List<OrgPost>    返回类型 
	 * @throws
	 */
	List<OrgPost> queryPostList(ReqBean<BaseQuery> bean);
}
