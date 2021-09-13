/**
 * @Title: ResourceController.java 
 * @Package com.gov.wiki.organization.controller
 * @Description: 资源管理控制器
 * @author cys 
 * @date 2019年11月7日 下午10:04:33 
 * @version V1.0 
 */
package com.gov.wiki.organization.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.gov.wiki.common.entity.system.PrivMemberRole;
import com.gov.wiki.common.entity.system.PrivRoleResource;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.organization.service.IMemberRoleService;
import com.gov.wiki.organization.service.IRoleResourceService;
import com.gov.wiki.system.service.IFileService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.organization.req.ResourceReq;
import com.gov.wiki.organization.req.query.ResourceQuery;
import com.gov.wiki.organization.service.IResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/resource")
@RestController
@Api(tags = "资源管理")
public class ResourceController {
	
	/**
	 * 注入resourceService
	 */
	@Autowired
	private IResourceService resourceService;

	@Autowired
	private IRoleResourceService roleResourceService;

	@Autowired
	private IFileService fileService;

	@Autowired
	private RedisManager redisManager;

	@Autowired
	private IMemberRoleService memberRoleService;

	/**
	 * @Title: saveOrUpdate 
	 * @Description: 新增或者修改资源信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改资源信息")
	@ControllerMonitor(description = "新增或者修改资源信息", operType = 2)
	public ResultBean<PrivResource> saveOrUpdate(@RequestBody ReqBean<ResourceReq> bean) {
		ResourceReq req = bean.getBody();
		PrivResource r = req == null?null:req.toEntity();
		return resourceService.saveOrUpdateEntity(r);
//		CheckUtil.notNull(r, ResultCode.COMMON_ERROR, "参数不存在！");
//		CheckClass.check(r);
//		if(r.getStatus() == null) {
//			r.setStatus(StatusEnum.ENABLE.getValue());
//		}
//		if(StringUtils.isBlank(r.getParentId())) {
//			r.setParentId("-1");
//		}
//		PrivResource privResource = resourceService.saveOrUpdate(r);
//		if(privResource!=null) return new ResultBean<>();
//		else return ResultBean.error(-1,"新增或保存失败");
	}

	/**
	 * @Title: delById 
	 * @Description: 根据ID删除资源信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/delByIds")
	@ApiOperation(value = "批量删除删除资源信息")
	@ControllerMonitor(description = "批量删除删除资源信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<List<String>> bean) {
		List<String> id = bean.getBody();
		List<PrivRoleResource> byResourceId = roleResourceService.findByResourceIds(id);
		if(byResourceId.size()!=0) return ResultBean.error(-1,"有角色绑定资源,无法删除");
		resourceService.batchDelByIds(id);

//		PrivResource r = resourceService.findById(id);
//		List<String> ids = new ArrayList<String>();
//		resourceService.iteratResourceTree(r, ids);
//		if(!ids.isEmpty()) {
//			resourceService.batchDelByIds(ids);
//		}
		return new ResultBean();
	}

	/**
	 * @Title: findById 
	 * @Description: 根据ID查询资源信息
	 * @param 设定文件 
	 * @return ResultBean<PrivResource>    返回类型 
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询资源信息")
	@ControllerMonitor(description = "根据ID查询资源信息")
	public ResultBean<PrivResource> findById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		PrivResource r = resourceService.findById(id);
		return new ResultBean(r);
	}

	/**
	 * @Title: findListByParams 
	 * @Description: 根据参数查询资源信息
	 * @param 设定文件 
	 * @return ResultBean<List<PrivResource>>    返回类型 
	 * @throws
	 */
	@PostMapping("/findListByParams")
	@ApiOperation(value = "根据参数查询资源信息")
	@ControllerMonitor(description = "根据参数查询资源信息")
	public ResultBean<List<PrivResource>> findListByParams(@RequestBody ReqBean<ResourceQuery> bean) {
		ResourceQuery r = bean.getBody();
		return new ResultBean(resourceService.findByParams(r));
	}

	@GetMapping("/resourceCatalog")
	@ApiOperation(value = "获取菜单目录")
	@ControllerMonitor(description = "获取菜单目录")
	public ResultBean<HashMap<String,Object>> catalog(HttpServletRequest request) {
//		String token = request.getHeader(Constants.TOKEN);
//		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
//		List<PrivMemberRole> byMemberId = memberRoleService.findByMemberId(user.getCreater());
//		List<String> roleids=new ArrayList<>();
//		for (PrivMemberRole privMemberRole : byMemberId) {
//			roleids.add(privMemberRole.getRoleId());
//		}
//		List<String> resourceids = roleResourceService.findByRoleIds(roleids);

		HashMap<String,Object> ans=new HashMap<>();
		List<Object[]> topid = resourceService.findIdByParentId("-1");
		ans.put("0",topid);
		List<Object[]> upMatterId = resourceService.groupParentId();
		for (Object[] s : upMatterId) {
			List<Object[]> idByUpmatterId = resourceService.findIdByParentId(s[0].toString());
			ans.put(s[0].toString(),idByUpmatterId);
		}
		return new ResultBean<>(ans);
	}
}