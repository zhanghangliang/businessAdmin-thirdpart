/**
 * @Title: IResourceService.java 
 * @Package com.gov.wiki.organization.service 
 * @Description: 资源管理Service接口
 * @author cys 
 * @date 2019年11月7日 下午9:49:05 
 * @version V1.0 
 */
package com.gov.wiki.organization.service;

import java.util.List;
import javax.transaction.Transactional;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.organization.req.query.ResourceQuery;

public interface IResourceService extends IBaseService<PrivResource, String> {

	/**
	 * @Title: iteratResourceTree 
	 * @Description: 迭代或者资源树
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void iteratResourceTree(PrivResource r, List<String> ids);
	
	/**
	 * @Title: batchDelByIds 
	 * @Description: 根据资源主键批量删除资源信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	void batchDelByIds(List<String> ids);
	
	/**
	 * @Title: findByParams 
	 * @Description: 根据查询条件查询资源信息
	 * @param 设定文件 
	 * @return List<PrivResource>    返回类型 
	 * @throws
	 */
	List<PrivResource> findByParams(ResourceQuery rq);
	
	/**
	 * @Title: findByRoleId 
	 * @Description: 根据角色查询资源信息
	 * @param 设定文件 
	 * @return List<PrivResource>    返回类型 
	 * @throws
	 */
	List<PrivResource> findByRoleId(List<String> ids);

	List<Object[]> groupParentId();

	List<Object[]> findIdByParentId(String parentid);

	List<PrivResource> findByParentId(String parentid,List<String> ids);

	List<PrivResource> findByParentIdAndIdIn(String parentid,List<String> ids);

	List<Object[]> groupParentId2(List<String> ids);

	List<Object[]> findIdByParentId2(String parentid,List<String> ids);
}