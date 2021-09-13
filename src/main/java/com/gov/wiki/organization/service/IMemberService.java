/**
 * @Title: IMemberService.java
 * @Package com.gov.wiki.organization.service.impl
 * @Description: 人员管理处理接口
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import java.util.List;
import com.gov.wiki.common.entity.MemberInfoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.ChangePwd;
import com.gov.wiki.organization.req.LoginReq;
import com.gov.wiki.organization.req.query.MemberQuery;
import com.gov.wiki.organization.res.LoginRes;

public interface IMemberService extends IBaseService<OrgMember, String> {

	/**
	 * @Title: userLogin
	 * @Description: 用户登录
	 * @param userName
	 * @param pwd
	 * @return LoginRes 返回类型
	 * @throws
	 */
	LoginRes userLogin(LoginReq req);
	
	/**
	 * @Title: saveOrUpdateMember 
	 * @Description: 新增或者修改用户信息
	 * @param 设定文件 
	 * @return ResultBean<OrgMember>    返回类型 
	 * @throws
	 */
	ResultBean<String> saveOrUpdateMember(OrgMember member);
	
	/**
	 * @Title: existSameUsername 
	 * @Description: 判断是否存在相同用户名
	 * @param 设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	boolean existSameUsername(String username, OrgMember old);
	
	/**
	 * @Title: delMemberById 
	 * @Description: 根据人员ID删除人员信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	ResultBean delMemberById(String memberId);
	
	/**
	 * @Title: batchDelMemberByIds 
	 * @Description: 批量删除人员信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	ResultBean batchDelMemberByIds(List<String> ids);
	
	/**
	 * @Title: pageMemberList 
	 * @Description: 分页查询人员信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	ResultBean<PageInfo> pageMemberList(ReqBean<MemberQuery> bean);
	
	/**
	 * @Title: countMember 
	 * @Description: 统计人员数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	int countMember();
	
	/**
	 * @Title: queryMemberList 
	 * @Description: 根据参数查询人员列表
	 * @param 设定文件 
	 * @return ResultBean<List<OrgMember>>    返回类型 
	 * @throws
	 */
	ResultBean<List<OrgMember>> queryMemberList(@RequestBody ReqBean<MemberQuery> bean);
	
	/**
	 * @Title: pageMembersByRoleId 
	 * @Description: 分页查询角色人员信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	ResultBean<PageInfo> pageMembersByRoleId(ReqBean<String> bean,List<String> creater);
	
	/**
	 * @Title: queryMemberByDepartPath 
	 * @Description: 查询部门下所有人员
	 * @param 设定文件 
	 * @return List<OrgMember>    返回类型 
	 * @throws
	 */
	List<OrgMember> queryMemberByDepartPath(String path);
	
	/**
	 * @Title: queryMemberByDepartPaths 
	 * @Description: 根据部门范围查询人员信息
	 * @param 设定文件 
	 * @return List<OrgMember>    返回类型 
	 * @throws
	 */
	List<OrgMember> queryMemberByDepartPaths(List<String> path, String userId);

	void changePwd(ChangePwd body);

	List<MemberInfoReq> findNameByIds(List<String> ids);
	
	/**
	 * @Title: getByUsername
	 * @Description: 根据用户名查询用户信息
	 * @param username
	 * @return OrgMember 返回类型
	 * @throws
	 */
	OrgMember getByUsername(String username);

	List<MemberInfoReq> findByDepartId(String id);

	List<MemberInfoReq> getAllmember(String companyId);

	Page<MemberInfoReq> findAll(Specification specification, PageRequest pageRequest);

	List<String> findIdByCompanyId(String companyid);
	
	/**
	 * @Title: findNamesByIds
	 * @Description: 根据人员id查询人员名称
	 * @param ids
	 * @return String 返回类型
	 * @throws
	 */
	String findNamesByIds(List<String> ids);
}