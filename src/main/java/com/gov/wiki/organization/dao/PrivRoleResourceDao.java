package com.gov.wiki.organization.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.PrivRoleResource;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface PrivRoleResourceDao extends BaseRepository<PrivRoleResource, String> {
	
	/**
	 * @Title: delRRByResourceIds 
	 * @Description: 根据资源ID删除角色资源关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query("delete from PrivRoleResource rr where rr.resourceId in (:resourceIds)")
	void delRRByResourceIds(@Param("resourceIds") List<String> resourceIds);
	
	/**
	 * @Title: delRRByRoleIds 
	 * @Description: 根据角色ID删除角色资源关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query("delete from PrivRoleResource rr where rr.roleId in (:RoleIds)")
	void delRRByRoleIds(@Param("RoleIds") List<String> RoleIds);
	
	/**
	 * @Title: deleteByRoleId 
	 * @Description: 根据角色删除角色资源信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	void deleteByRoleId(String roleId);
	
	/**
	 * @Title: findByRoleId 
	 * @Description: 根据角色
	 * @param 设定文件 
	 * @return List<PrivRoleResource>    返回类型 
	 * @throws
	 */
	List<PrivRoleResource> findByRoleId(String roleId);

	List<PrivRoleResource> findByResourceId(String resourceId);

	@Query("select t from PrivRoleResource t where t.resourceId in (?1)")
	List<PrivRoleResource> findByResourceIds(List<String> ids);

	@Query("select t.resourceId from PrivRoleResource t where t.roleId in (?1)")
	List<String> findByRoleIds(List<String> roleIds);
}