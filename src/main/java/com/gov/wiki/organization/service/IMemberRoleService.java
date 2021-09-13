/**
 * @Title: IMemberRoleService.java
 * @Package com.spm.system.service
 * @Description: 人员角色管理处理接口
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import java.util.List;

import com.gov.wiki.common.entity.system.PrivMemberRole;
import com.gov.wiki.common.service.IBaseService;
import org.springframework.data.repository.query.Param;

public interface IMemberRoleService extends IBaseService<PrivMemberRole, String>{
	
	/**
	 * @Title: findByRoleId 
	 * @Description: 根据角色ID查询角色资源信息
	 * @param 设定文件 
	 * @return List<PrivMemberRole>    返回类型 
	 * @throws
	 */
	List<PrivMemberRole> findByRoleId(String roleId);

	/**
	 * @Title: delByRoleId 
	 * @Description: 根据角色ID删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void delByRoleId(String roleId);
	
	/**
	 * @Title: batchDelByRoleIds 
	 * @Description: 根据角色ID批量删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void batchDelByRoleIds(List<String> roleIds);
	
	/**
	 * @Title: updateMemberRole 
	 * @Description: 更新用户角色关系信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */

	void updateMemberRole(String memberId, List<String> roleIds);
	
	/**
	 * @Title: delByMemberId 
	 * @Description: 根据人员删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void delByMemberId(String memberId);
	
	/**
	 * @Title: batchDelByMemberIds 
	 * @Description: 根据人员批量删除人员角色关系
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void batchDelByMemberIds(List<String> memberIds);
	
	/**
	 * @Title: updateRoleMembers 
	 * @Description: 更新角色人员信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	void updateRoleMembers(String roleId, List<String> memberIds);

	List<PrivMemberRole> findByRoleIds(List<String> ids);

	List<PrivMemberRole> findByMemberId(List<String> id);
}