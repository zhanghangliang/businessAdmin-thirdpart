package com.gov.wiki.organization.controller;

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
import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.BaseQuery;
import com.gov.wiki.organization.req.JobReq;
import com.gov.wiki.organization.service.IJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: JobController
 * @Description: 职务管理服务提供控制类
 * @author cys
 * @date 2019年11月4日 下午7:04:58
 */
@RequestMapping("/job")
@RestController
@Api(tags = "职务管理")
public class JobController {

	/**
	 * 注入jobService
	 */
	@Autowired
	private IJobService jobService;

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改职务信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/save-update")
	@ApiOperation(value = "新增或者修改职务信息")
	@ControllerMonitor(description = "新增或者修改职务信息", operType = 2)
	public ResultBean<OrgJob> saveOrUpdate(@RequestBody ReqBean<JobReq> bean) {
		JobReq req = bean.getBody();
		OrgJob job = req != null?req.toEntity():null;
		CheckUtil.notNull(job, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(job);
		return jobService.saveOrUpdateEntity(job);
	}

	/**
	 * @Title: delById
	 * @Description: 根据ID删除职务信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据ID删除职务信息")
	@ControllerMonitor(description = "根据ID删除职务信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgJob job = jobService.findById(id);
		if(job != null) {
			job.setDelFlag(true);
			jobService.saveOrUpdate(job);
		}
		return new ResultBean();
	}

	/**
	 * @Title: batchDel
	 * @Description: 批量删除职务信息
	 * @param 设定文件
	 * @return ResultBean    返回类型
	 * @throws
	 */
	@PostMapping("/batchDel")
	@ApiOperation(value = "批量删除职务信息")
	@ControllerMonitor(description = "批量删除职务信息", operType = 4)
	public ResultBean batchDel(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数为空");
		List<OrgJob> jobList = jobService.findByIds(ids);
		if(jobList != null && !jobList.isEmpty()) {
			for(OrgJob job:jobList) {
				job.setDelFlag(true);
			}
			jobService.saveAll(jobList);
		}
		return new ResultBean();
	}

	/**
	 * @Title: findById
	 * @Description: 根据ID查询职务信息
	 * @param 设定文件
	 * @return ResultBean<OrgJob>    返回类型
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据ID查询职务信息")
	@ControllerMonitor(description = "根据ID查询职务信息")
	public ResultBean<OrgJob> findById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgJob job = jobService.findById(id);
		return new ResultBean(job);
	}

	/**
	 * @Title: pageJobs
	 * @Description: 分页查询职务级别信息
	 * @param 设定文件
	 * @return ResultBean<PageInfo>    返回类型
	 * @throws
	 */
	@PostMapping("/pageJob")
	@ApiOperation(value = "分页查询职务级别信息")
	@ControllerMonitor(description = "分页查询职务级别信息")
	public ResultBean<PageInfo> pageJobs(@RequestBody ReqBean<BaseQuery> bean) throws InstantiationException, IllegalAccessException {
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(StringMatcher.CONTAINING)
				.withIgnoreNullValues();
		OrgJob job = new OrgJob();
		BaseQuery query = bean.getBody();
		if(query != null) {
			job.setName(query.getKeywords());
		}
		ReqBean<OrgJob> reqBean = new ReqBean<OrgJob>();
		reqBean.setBody(job);
		reqBean.setHeader(bean.getHeader());
		return jobService.pageList(reqBean, OrgJob.class, matcher);
	}

	/**
	 * @Title: queryJobList
	 * @Description: 根据参数查询职务列表
	 * @param 设定文件
	 * @return ResultBean<List<OrgJob>> 返回类型
	 * @throws
	 */
	@PostMapping("/queryJobList")
	@ApiOperation(value = "根据参数查询职务列表")
	@ControllerMonitor(description = "根据参数查询职务列表")
	public ResultBean<List<OrgJob>> queryJobList(@RequestBody ReqBean<BaseQuery> bean) {
		List<OrgJob> jobList = jobService.queryJobList(bean);
		return new ResultBean(jobList);
	}
}