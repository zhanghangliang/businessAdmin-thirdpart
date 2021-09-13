package com.gov.wiki.organization.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.BaseQuery;
import com.gov.wiki.organization.req.PostReq;
import com.gov.wiki.organization.service.IPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: PostController
 * @Description: 岗位控制器
 * @author cys
 * @date 2019年11月4日 下午8:52:23
 */
@RequestMapping("/post")
@RestController
@Api(tags = "岗位管理")
public class PostController {
	/**
	 * 注入postService
	 */
	@Autowired
	private IPostService postService;

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改岗位信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改岗位信息")
	@ControllerMonitor(description = "新增或者修改岗位信息", operType = 2)
	public ResultBean<OrgPost> saveOrUpdate(@RequestBody ReqBean<PostReq> bean) {
		PostReq req = bean.getBody();
		OrgPost post = null;
		if(req != null) {
			post = req.toEntity();
		}
		CheckUtil.notNull(post, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(post);
		return postService.saveOrUpdateEntity(post);
	}

	/**
	 * @Title: delById
	 * @Description: 根据ID删除岗位信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据ID删除岗位信息")
	@ControllerMonitor(description = "根据ID删除岗位信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgPost post = postService.findById(id);
		if(post != null) {
			post.setDelFlag(true);
			postService.saveOrUpdate(post);
		}
		return new ResultBean();
	}

	/**
	 * @Title: batchDel
	 * @Description: 批量删除岗位信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/batchDel")
	@ApiOperation(value = "批量删除岗位信息")
	@ControllerMonitor(description = "批量删除岗位信息", operType = 4)
	public ResultBean batchDel(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数为空");
		List<OrgPost> postList = postService.findByIds(ids);
		if(postList != null && !postList.isEmpty()) {
			for(OrgPost post:postList) {
				post.setDelFlag(true);
			}
			postService.saveAll(postList);
		}
		return new ResultBean();
	}

	/**
	 * @Title: findById
	 * @Description: 根据ID查询岗位信息
	 * @param 设定文件
	 * @return ResultBean<OrgPost>    返回类型
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询岗位信息")
	@ControllerMonitor(description = "根据ID查询岗位信息")
	public ResultBean<OrgPost> findById(@RequestBody ReqBean<String> bean){
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgPost post = postService.findById(id);
		return new ResultBean(post);
	}

	/**
	 * @Title: pagePost
	 * @Description: 分页查询岗位信息
	 * @param 设定文件
	 * @return ResultBean<PageInfo>    返回类型
	 * @throws
	 */
	@PostMapping("/pagePost")
	@ApiOperation(value = "分页查询岗位信息")
	@ControllerMonitor(description = "分页查询岗位信息")
	public ResultBean<PageInfo> pagePost(@RequestBody ReqBean<BaseQuery> bean) throws InstantiationException, IllegalAccessException{
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(StringMatcher.CONTAINING)
				.withIgnoreNullValues();
		OrgPost post = new OrgPost();
		BaseQuery query = bean.getBody();
		if(query != null) {
			post.setName(query.getKeywords());
		}
		ReqBean<OrgPost> reqBean = new ReqBean<OrgPost>();
		reqBean.setBody(post);
		reqBean.setHeader(bean.getHeader());
		return postService.pageList(reqBean, OrgPost.class, matcher);
	}

	/**
	 * @Title: queryPostList
	 * @Description: 根据参数查询岗位列表
	 * @param 设定文件
	 * @return ResultBean<List<OrgPost>>    返回类型
	 * @throws
	 */
	@PostMapping("/queryPostList")
	@ApiOperation(value = "根据参数查询岗位列表")
	@ControllerMonitor(description = "根据参数查询岗位列表")
	public ResultBean<List<OrgPost>> queryPostList(@RequestBody ReqBean<BaseQuery> bean) {
		List<OrgPost> pList = postService.queryPostList(bean);
		return new ResultBean(pList);
	}
}