/**
 * @Title: IRoleService.java
 * @Package com.gov.wiki.organization.service
 * @Description: 角色管理处理接口
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import java.util.HashMap;
import java.util.List;
import javax.transaction.Transactional;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.DistribMemberReq;
import com.gov.wiki.organization.req.DistribResourceReq;
import com.gov.wiki.organization.req.query.RoleQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface IRoleService extends IBaseService<OrgRole, String> {

	/**
	 * @Title: delRoleByIds 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	void delRoleById(String id);
	
	/**
	 * @Title: batchDelRoleByIds 
	 * @Description: 根据id批量删除角色
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	void batchDelRoleByIds(List<String> ids);
	
	/**
	 * @Title: findByUserId 
	 * @Description: 根据用户ID查询角色信息
	 * @param 设定文件 
	 * @return List<OrgRole>    返回类型 
	 * @throws
	 */
	List<OrgRole> findByUserId(String userId);
	
	/**
	 * @Title: distribMember 
	 * @Description: 分配角色人员信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	ResultBean distribMember(DistribMemberReq req);
	
	/**
	 * @Title: distribResource 
	 * @Description: 分配角色资源信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	ResultBean distribResource(DistribResourceReq req);
	
	/**
	 * @Title: queryMembersByRoleId 
	 * @Description: 根据角色ID查询人员信息
	 * @param 设定文件 
	 * @return ResultBean<List<OrgMember>>    返回类型 
	 * @throws
	 */
	ResultBean<List<OrgMember>> queryMembersByRoleId(String roleId);
	
	/**
	 * @Title: queryResourcesByRoleId 
	 * @Description: 根据角色ID查询资源信息
	 * @param 设定文件 
	 * @return ResultBean<List<PrivResource>>    返回类型 
	 * @throws
	 */
	ResultBean<HashMap<String,Object>> queryResourcesByRoleId(String roleId);
	
	/**
	 * @Title: pageRoleList 
	 * @Description: 分页查询角色信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	ResultBean<PageInfo> pageRoleList(ReqBean<RoleQuery> bean);
	
	/**
	 * @Title: queryRoleList 
	 * @Description: 根据参数查询角色列表
	 * @param 设定文件 
	 * @return ResultBean<List<OrgRole>>    返回类型 
	 * @throws
	 */
	ResultBean<List<OrgRole>> queryRoleList(ReqBean<RoleQuery> bean,List<String> creater);

	Page<OrgRole> findAll(Specification specification, PageRequest pageRequest);
}