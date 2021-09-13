package com.gov.wiki.organization.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.PrivMemberRole;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface PrivMemberRoleDao extends BaseRepository<PrivMemberRole, String> {
	
	/**
	 * @Title: deleteByRoleId 
	 * @Description: 根据角色ID删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	void deleteByRoleId(String roleId);
	
	/**
	 * @Title: batchDelByRoleIds 
	 * @Description: 根据角色ID批量删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Modifying
	@Transactional
	@Query("delete from PrivMemberRole where roleId in(:roleIds)")
	void batchDelByRoleIds(@Param("roleIds") List<String> roleIds);
	
	/**
	 * @Title: deleteByMemberId 
	 * @Description: 根据人员删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	void deleteByMemberId(String memberId);
	
	/**
	 * @Title: batchDelByMemberIds 
	 * @Description: 根据人员批量删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Modifying
	@Transactional
	@Query("delete from PrivMemberRole where memberId in(:memberIds)")
	void batchDelByMemberIds(@Param("memberIds") List<String> memberIds);
	
	/**
	 * @Title: findByRoleId 
	 * @Description: 根据角色ID查询角色人员信息
	 * @param 设定文件 
	 * @return List<PrivMemberRole>    返回类型 
	 * @throws
	 */
	List<PrivMemberRole> findByRoleId(String roleId);

	@Query("select t from PrivMemberRole t where t.roleId in(:ids)")
	List<PrivMemberRole> findByRoleIds(@Param("ids") List<String> ids);

	List<PrivMemberRole> findByMemberIdIn(List<String> memberid);
}