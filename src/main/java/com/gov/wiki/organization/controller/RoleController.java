/**
 * @Title: RoleController.java 
 * @Package com.gov.wiki.organization.controller
 * @Description: 角色管理控制器
 * @author cys 
 * @date 2019年11月6日 下午9:25:13 
 * @version V1.0 
 */
package com.gov.wiki.organization.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.entity.system.*;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.organization.service.IMemberRoleService;
import com.gov.wiki.organization.service.IRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.organization.req.DistribMemberReq;
import com.gov.wiki.organization.req.DistribResourceReq;
import com.gov.wiki.organization.req.RoleReq;
import com.gov.wiki.organization.req.query.RoleQuery;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.organization.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/role")
@RestController
@Api(tags = "角色管理")
public class RoleController{
	
	/**
	 * 注入roleService
	 */
	@Autowired
	private IRoleService roleService;
	
	/**
	 * 注入roleService
	 */
	@Autowired
	private IMemberService memberService;

	@Autowired
	private IMemberRoleService memberRoleService;

	@Autowired
	private IRoleResourceService roleResourceService;

	@Autowired
	private RedisManager redisManager;


	/**
	 * @Title: saveOrUpdate 
	 * @Description: 新增或者修改角色信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改角色信息")
	@ControllerMonitor(description = "新增或者修改角色信息", operType = 2)
	public ResultBean<OrgRole> saveOrUpdate(@RequestBody ReqBean<RoleReq> bean) {
		RoleReq req = bean.getBody();
		OrgRole role = req == null?null:req.toEntity();
		CheckUtil.notNull(role, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(role);
		return roleService.saveOrUpdateEntity(role);
	}

	/**
	 * @Title: delById 
	 * @Description: 根据ID删除角色信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
//	@PostMapping("/delById")
//	@ApiOperation(value = "根据ID删除角色信息")
//	@ControllerMonitor(description = "根据ID删除角色信息", operType = 4)
//	public ResultBean delById(@RequestBody ReqBean<String> bean) {
//		String id = bean.getBody();
//		List<PrivMemberRole> byRoleId = memberRoleService.findByRoleId(id);
//		if(byRoleId.size()!=0) return ResultBean.error(-1,"有人员绑定角色,无法删除");
//		List<String> ids=new ArrayList<>();
//		ids.add(id);
//		roleResourceService.delRRByRoleIds(ids);
//		roleService.deleteById(id);
//
//		return new ResultBean();
//	}

	/**
	 * @Title: batchDel 
	 * @Description: 批量删除角色信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/batchDel")
	@ApiOperation(value = "批量删除角色信息")
	@ControllerMonitor(description = "批量删除角色信息", operType = 4)
	public ResultBean batchDel(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数为空");
		List<PrivMemberRole> byRoleIds = memberRoleService.findByRoleIds(ids);
		if(byRoleIds.size()!=0) return ResultBean.error(-1,"被选择角色中有人员绑定,无法删除");
		roleService.batchDelete(ids);
		roleResourceService.delRRByRoleIds(ids);
		return new ResultBean();
	}

	/**
	 * @Title: findById 
	 * @Description: 根据ID查询角色信息
	 * @param 设定文件 
	 * @return ResultBean<OrgRole>    返回类型 
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询角色信息")
	@ControllerMonitor(description = "根据ID查询角色信息")
	public ResultBean<OrgRole> findById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		return new ResultBean(roleService.findById(id));
	}

	/**
	 * @Title: pageRole 
	 * @Description: 分页查询角色信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	@PostMapping("/pageRole")
	@ApiOperation(value = "分页查询角色信息")
	@ControllerMonitor(description = "分页查询角色信息", operType = 2)
	public ResultBean<Page<OrgRole>> pageRole(@RequestBody ReqBean<String> bean, HttpServletRequest request) {
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		Specification<OrgRole> specification = Specifications.<OrgRole>and()
				.in("createBy",user.getCreater())
				.build();
		ReqHeader header = bean.getHeader();
		Sort sort = header.getSort();

		return new ResultBean<>(roleService.findAll(specification, PageRequest.of(header.getPageNumber(),header.getPageSize(),sort)));
	}

	/**
	 * @Title: distribMember 
	 * @Description: 分配角色人员
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/distribMember")
	@ApiOperation(value = "分配角色人员")
	@ControllerMonitor(description = "分配角色人员", operType = 3)
	public ResultBean distribMember(@RequestBody ReqBean<DistribMemberReq> bean) {
		DistribMemberReq req = bean.getBody();
		CheckUtil.check(req != null, ResultCode.COMMON_ERROR, "请求参数为空");
		CheckUtil.check(StringUtils.isNotBlank(req.getRoleId()), ResultCode.COMMON_ERROR, "请求参数角色信息为空");
		return roleService.distribMember(req);
	}

	/**
	 * @Title: distribResource 
	 * @Description: 分配角色资源
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/distribResource")
	@ApiOperation(value = "分配角色资源")
	@ControllerMonitor(description = "分配角色资源", operType = 3)
	public ResultBean distribResource(@RequestBody ReqBean<DistribResourceReq> bean) {
		DistribResourceReq req = bean.getBody();
		CheckUtil.check(req != null, ResultCode.COMMON_ERROR, "请求参数为空");
		CheckUtil.check(StringUtils.isNotBlank(req.getRoleId()), ResultCode.COMMON_ERROR, "请求参数角色信息为空");
		return roleService.distribResource(req);
	}

	/**
	 * @Title: queryMembersByRoleId 
	 * @Description: 根据角色ID查询人员信息
	 * @param 设定文件 
	 * @return ResultBean<List<OrgMember>>    返回类型 
	 * @throws
	 */
	@PostMapping("/queryMembersByRoleId")
	@ApiOperation(value = "根据角色ID查询人员信息")
	@ControllerMonitor(description = "根据角色ID查询人员信息")
	public ResultBean<List<OrgMember>> queryMembersByRoleId(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "请求参数为空");

		return roleService.queryMembersByRoleId(id);
	}

	/**
	 * @Title: queryResourcesByRoleId 
	 * @Description: 根据角色ID查询资源信息
	 * @param 设定文件 
	 * @return ResultBean<List<PrivResource>>    返回类型 
	 * @throws
	 */
	@PostMapping("/queryResourcesByRoleId")
	@ApiOperation(value = "根据角色ID查询资源信息")
	@ControllerMonitor(description = "根据角色ID查询资源信息")
	public ResultBean<HashMap<String,Object>> queryResourcesByRoleId(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "请求参数为空");
		return roleService.queryResourcesByRoleId(id);
	}

	/**
	 * @Title: pageMembersByRoleId 
	 * @Description: 分页查询角色用户信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	@PostMapping("/pageMembersByRoleId")
	@ApiOperation(value = "分页查询角色用户信息")
	@ControllerMonitor(description = "分页查询角色用户信息")
	public ResultBean<PageInfo> pageMembersByRoleId(@RequestBody ReqBean<String> bean, HttpServletRequest request) {
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		String roleId = bean.getBody();
		CheckUtil.notEmpty(roleId, ResultCode.COMMON_ERROR, "请求参数为空");
		return memberService.pageMembersByRoleId(bean,user.getCreater());
	}

	/**
	 * @Title: queryRoleList 
	 * @Description: 根据参数查询角色列表
	 * @param 设定文件 
	 * @return ResultBean<List<OrgRole>>    返回类型 
	 * @throws
	 */
	@PostMapping("/queryRoleList")
	@ApiOperation(value = "根据参数查询角色列表")
	@ControllerMonitor(description = "根据参数查询角色列表")
	public ResultBean<List<OrgRole>> queryRoleList(@RequestBody ReqBean<RoleQuery> bean, HttpServletRequest request) {
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		return roleService.queryRoleList(bean,user.getCreater());
	}
}