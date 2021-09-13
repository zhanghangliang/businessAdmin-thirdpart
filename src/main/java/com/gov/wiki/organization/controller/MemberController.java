/**
 * @Title: MemberController.java 
 * @Package com.gov.wiki.organization.controller
 * @Description: 人员管理控制器
 * @author cys 
 * @date 2019年11月8日 下午4:34:37 
 * @version V1.0 
 */
package com.gov.wiki.organization.controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.req.MaterialQuery;
import com.gov.wiki.common.entity.MemberInfoReq;
import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.PrivMemberRole;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.organization.req.PageMemberReq;
import com.gov.wiki.organization.service.IDepartService;
import com.gov.wiki.organization.service.IMemberRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.organization.req.ChangePwd;
import com.gov.wiki.organization.req.MemberReq;
import com.gov.wiki.organization.req.query.MemberQuery;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.organization.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/member")
@RestController
@Api(tags = "人员管理")
public class MemberController {
	
	/**
	 * 注入memberService
	 */
	@Autowired
	private IMemberService memberService;
	/**
	 * 注入roleService
	 */
	@Autowired
	private IRoleService roleService;

	@Autowired
	private IMemberRoleService memberRoleService;

	@Autowired
	private IDepartService departService;

	@Autowired
	private RedisManager redisManager;

	/**
	 * @Title: saveOrUpdate 
	 * @Description: 新增或者修改人员信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改人员信息")
	@ControllerMonitor(description = "新增或者修改人员信息", operType = 2)
	public ResultBean<String> saveOrUpdate(@RequestBody ReqBean<MemberReq> bean) {
		MemberReq req = bean.getBody();
		OrgMember member = req != null?req.toEntity():null;
		CheckUtil.notNull(member, ResultCode.COMMON_ERROR, "参数不存在！");
		OrgMember byUsername = memberService.getByUsername(req.getUsername());
		CheckUtil.check(!(byUsername!=null && !byUsername.getId().equals(req.getId())),ResultCode.COMMON_ERROR,"用户名已存在");

		List<String> roleIds = req.getRoleList();
		memberRoleService.updateMemberRole(req.getId(),roleIds);

		CheckUtil.check(!(member.getPassword()==null && (member.getId()==null || "".equals(member.getPassword()))),ResultCode.COMMON_ERROR,"密码为空");

		if(member.getPassword()==null || "".equals(member.getPassword())){
			member.setPassword(byUsername.getPassword());
		}
		else{
			member.setPassword(PasswordUtil.getSaltMD5(member.getPassword()));
		}
		return memberService.saveOrUpdateMember(member);
	}

	/**
	 * @Title: delById 
	 * @Description: 根据ID删除岗位信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
//	@PostMapping("/delById")
//	@ApiOperation(value = "根据ID删除岗位信息")
//	@ControllerMonitor(description = "根据ID删除岗位信息", operType = 4)
//	public ResultBean delById(@RequestBody ReqBean<String> bean) {
//		String id = bean.getBody();
//		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
//		memberService.deleteById(id);
//		memberRoleService.delByMemberId(id);
//		OrgMember byId = memberService.findById(id);
//		if(byId==null) return new ResultBean();
//		else return ResultBean.error(-1,"删除失败");
//	}


	/**
	 * @Title: batchDel 
	 * @Description: 批量删除人员信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/batchDel")
	@ApiOperation(value = "批量删除人员信息")
	@ControllerMonitor(description = "批量删除人员信息", operType = 4)
	public ResultBean batchDel(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数为空");
		memberService.batchDelete(ids);
		memberRoleService.batchDelByMemberIds(ids);
		OrgMember byId = memberService.findById(ids.get(0));
		return byId==null? new ResultBean():ResultBean.error(-1,"删除失败");
	}

	/**
	 * @Title: findById 
	 * @Description: 根据ID查询人员信息
	 * @param 设定文件 
	 * @return ResultBean<OrgMember>    返回类型 
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询人员信息")
	@ControllerMonitor(description = "根据ID查询人员信息")
	public ResultBean<OrgMember> findById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgMember member = memberService.findById(id);
		if(member != null) {
			member.setRoleList(roleService.findByUserId(id));
		}
		return new ResultBean(member);
	}

	/**
	 * @Title: pageMember 
	 * @Description: 分页查询人员信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	@PostMapping("/pageMember")
	@ApiOperation(value = "分页查询人员信息")
	@ControllerMonitor(description = "分页查询人员信息")
	public ResultBean<Page<OrgMember>> pageMember(@RequestBody ReqBean<PageMemberReq> bean, HttpServletRequest request) {
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		OrgDepart byId = departService.findById(bean.getBody().getDepartId());
		Specification<OrgMember> specification1 = Specifications.<OrgMember>and()
				.eq(StringUtils.isNotBlank(bean.getBody().getCompanyId()),"companyId",bean.getBody().getCompanyId())
				.eq(StringUtils.isNotBlank(bean.getBody().getDepartId()),"depart",byId)
				.in("createBy",user.getCreater())
				.build();
		ReqHeader header = bean.getHeader();
		Sort sort = header.getSort();
		return new ResultBean(memberService.findAll(specification1, PageRequest.of(header.getPageNumber(),header.getPageSize(),sort)));
	}

	/**
	 * @Title: queryMemberList 
	 * @Description: 根据参数查询人员列表
	 * @param 设定文件 
	 * @return ResultBean<List<OrgMember>>    返回类型 
	 * @throws
	 */
//	@PostMapping("/queryMemberList")
//	@ApiOperation(value = "根据参数查询人员列表")
//	@ControllerMonitor(description = "根据参数查询人员列表")
//	public ResultBean<List<OrgMember>> queryMemberList(@RequestBody ReqBean<MemberQuery> bean) {
//		return memberService.queryMemberList(bean);
//	}

	/**
	 * @Title: changePwd
	 * @Description: 修改密码
	 * @param bean
	 * @return void 返回类型
	 * @throws
	 */
//	@PostMapping("/changePwd")
//	@ApiOperation(value = "根据参数查询人员列表")
//	@ControllerMonitor(description = "根据参数查询人员列表", operType = 3)
//	public ResultBean changePwd(@RequestBody ReqBean<ChangePwd> bean) {
//		memberService.changePwd(bean.getBody());
//		return new ResultBean();
//	}

	@PostMapping("/getAllmember")
	@ApiOperation(value = "获取所有人员编号和姓名")
	@ControllerMonitor(description = "获取所有人员编号和姓名")
	public ResultBean<List<MemberInfoReq>> getAllmember(@RequestBody ReqBean<String> bean){
		return new ResultBean<>(memberService.getAllmember(bean.getBody()));
	}

	@PostMapping("/getAllmemberByDepart")
	@ApiOperation(value = "根据部门获取所有人员编号和姓名")
	@ControllerMonitor(description = "根据部门获取所有人员编号和姓名")
	public ResultBean<List<MemberInfoReq>> getAllmemberByDepart(@RequestBody ReqBean<String> bean){
		return new ResultBean<>(memberService.findByDepartId(bean.getBody()));
	}
}