/**
 * @Title: IRoleResourceService.java 
 * @Package com.insolu.spm.organization.service 
 * @Description: 角色资源关系管理Service接口
 * @author cys 
 * @date 2019年11月7日 下午9:52:02 
 * @version V1.0 
 */
package com.gov.wiki.organization.service;

import java.util.List;
import com.gov.wiki.common.entity.system.PrivRoleResource;
import com.gov.wiki.common.service.IBaseService;

public interface IRoleResourceService extends IBaseService<PrivRoleResource, String>{

	/**
	 * @Title: delRoleResourceByResourceIds 
	 * @Description: 根据资源ID批量删除角色资源关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void delRRByResourceIds(List<String> resourceIds);
	
	/**
	 * @Title: delRRByRoleIds 
	 * @Description: 根据角色ID批量删除角色资源关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void delRRByRoleIds(List<String> roleIds);
	
	/**
	 * @Title: updateRoleResource 
	 * @Description: 更新角色资源信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void updateRoleResource(String roleId, List<String> resourceIds);
	
	/**
	 * @Title: findByRoleId 
	 * @Description: 根据ID查询角色资源信息
	 * @param 设定文件 
	 * @return List<PrivRoleResource>    返回类型 
	 * @throws
	 */
	List<PrivRoleResource> findByRoleId(String roleId);

	List<PrivRoleResource> findByResourceId(String resourceId);

	List<PrivRoleResource> findByResourceIds(List<String> ids);

	List<String> findByRoleIds(List<String> roleIds);
}