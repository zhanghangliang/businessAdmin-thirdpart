package com.gov.wiki.organization.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.OrgGroup;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.req.GroupReq;
import com.gov.wiki.organization.req.query.GroupQuery;
import com.gov.wiki.organization.service.IDepartService;
import com.gov.wiki.organization.service.IGroupService;
import com.gov.wiki.organization.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: GroupController
 * @Description: 组管理控制器
 * @author cys
 * @date 2019年11月4日 下午9:00:02
 */
@RequestMapping("/group")
@RestController
@Api(tags = "组管理")
public class GroupController {
	/**
	 * 注入groupService
	 */
	@Autowired
	private IGroupService groupService;

	/**
	 * 注入memberService
	 */
	@Autowired
	private IMemberService memberService;

	/**
	 * 注入departService
	 */
	@Autowired
	private IDepartService departService;

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改组信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改组信息")
	@ControllerMonitor(description = "新增或者修改组信息", operType = 2)
	public ResultBean<OrgGroup> saveOrUpdate(@RequestBody ReqBean<GroupReq> bean) {
		GroupReq groupReq = bean.getBody();
		OrgGroup group = null;
		if(groupReq != null){
			group = groupReq.toEntity();
		}
		CheckUtil.notNull(group, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(group);
		return groupService.saveOrUpdateEntity(group);
	}

	/**
	 * @Title: delById
	 * @Description: 根据ID删除组信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据ID删除组信息")
	@ControllerMonitor(description = "根据ID删除组信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgGroup group = groupService.findById(id);
		if(group != null) {
			group.setDelFlag(true);
			groupService.saveOrUpdate(group);
		}
		return new ResultBean();
	}

	/**
	 * @Title: batchDel
	 * @Description: 批量删除组信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/batchDel")
	@ApiOperation(value = "批量删除组信息")
	@ControllerMonitor(description = "批量删除组信息", operType = 4)
	public ResultBean batchDel(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数为空");
		List<OrgGroup> groupList = groupService.findByIds(ids);
		if(groupList != null && !groupList.isEmpty()) {
			for(OrgGroup group:groupList) {
				group.setDelFlag(true);
			}
			groupService.saveAll(groupList);
		}
		return new ResultBean();
	}

	/**
	 * @Title: findById
	 * @Description: 根据ID查询组信息
	 * @param 设定文件
	 * @return ResultBean<OrgGroup>    返回类型
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询组信息")
	@ControllerMonitor(description = "根据ID查询组信息")
	public ResultBean<OrgGroup> findById(@RequestBody ReqBean<String> bean){
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgGroup group = groupService.findById(id);
		if(group != null) {
			if(StringUtils.isNotBlank(group.getMember())) {
				List<String> memberIds = Arrays.asList(group.getMember().split(","));
				group.setMemberList(memberService.findByIds(memberIds));
			}
			if(StringUtils.isNotBlank(group.getPublicRange())) {
				List<String> departIds = Arrays.asList(group.getPublicRange().split(","));
				group.setPublicRangeList(departService.findByIds(departIds));
			}
		}
		return new ResultBean(group);
	}

	/**
	 * @Title: pageGroup
	 * @Description: 分页查询组信息
	 * @param 设定文件
	 * @return ResultBean<PageInfo>    返回类型
	 * @throws
	 */
	@PostMapping("/pageGroup")
	@ApiOperation(value = "分页查询组信息")
	@ControllerMonitor(description = "分页查询组信息")
	public ResultBean<PageInfo> pageGroup(@RequestBody ReqBean<GroupQuery> bean) throws InstantiationException, IllegalAccessException{
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(StringMatcher.CONTAINING)
				.withIgnoreNullValues();
		OrgGroup group = new OrgGroup();
		GroupQuery query = bean.getBody();
		if(query != null) {
			group.setName(query.getKeywords());
		}
		ReqBean<OrgGroup> reqBean = new ReqBean<OrgGroup>();
		reqBean.setBody(group);
		reqBean.setHeader(bean.getHeader());
		return groupService.pageList(reqBean, OrgGroup.class, matcher);
	}
}