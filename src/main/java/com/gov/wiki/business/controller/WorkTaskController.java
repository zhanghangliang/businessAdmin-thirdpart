/**
 * @Title: WorkTaskController.java
 * @Package com.gov.wiki.business.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.WorkTaskReq;
import com.gov.wiki.business.service.IWorkTaskService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizWorkTask;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.organization.req.BaseQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: WorkTaskController
 * @Description: 工作任务管理控制器
 * @author cys
 * @date 2020年11月25日
 */
@RequestMapping("/work-task")
@RestController
@Api(tags = "工作任务管理")
public class WorkTaskController {

	/**
	 * 注入workTaskService
	 */
	@Autowired
	private IWorkTaskService workTaskService;
	
	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改工作任务信息
	 * @param bean
	 * @return ResultBean<BizWorkTask> 返回类型
	 * @throws
	 */
	@PostMapping("/save-update")
    @ApiOperation(value = "新增或者修改工作任务信息")
    @ControllerMonitor(description = "新增或者修改工作任务信息", operType = 8)
	public ResultBean<BizWorkTask> saveOrUpdate(@RequestBody ReqBean<WorkTaskReq> bean){
		WorkTaskReq req = bean.getBody();
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		BizWorkTask task = BeanUtils.copyProperties(req, BizWorkTask.class);
		task = workTaskService.saveOrUpdate(task);
		return new ResultBean<BizWorkTask>(task);
	}
	
	/**
	 * @Title: findById
	 * @Description: 根据ID查询工作任务信息
	 * @param bean
	 * @return ResultBean<BizWorkTask> 返回类型
	 * @throws
	 */
	@PostMapping("/findById")
    @ApiOperation(value = "根据ID查询工作任务信息")
    @ControllerMonitor(description = "根据ID查询工作任务信息")
	public ResultBean<BizWorkTask> findById(@RequestBody ReqBean<String> bean){
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "请求参数不存在");
		BizWorkTask task = workTaskService.findById(id);
		return new ResultBean<BizWorkTask>(task);
	}
	
	/**
	 * @Title: batchDel
	 * @Description: 批量删除工作任务信息
	 * @param bean
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	@PostMapping("/batchDel")
    @ApiOperation(value = "批量删除工作任务信息")
    @ControllerMonitor(description = "批量删除工作任务信息", operType = 4)
	public ResultBean<String> batchDel(@RequestBody ReqBean<List<String>> bean){
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数不存在");
		workTaskService.batchDelete(ids);
		return new ResultBean<String>();
	}
	
	/**
	 * @Title: delById
	 * @Description: 根据ID删除工作任务信息
	 * @param bean
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	@PostMapping("/delById")
    @ApiOperation(value = "根据ID删除工作任务信息")
    @ControllerMonitor(description = "根据ID删除工作任务信息", operType = 4)
	public ResultBean<String> delById(@RequestBody ReqBean<String> bean){
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "请求参数不存在");
		workTaskService.deleteById(id);
		return new ResultBean<String>();
	}
	
	/**
	 * @Title: pageList
	 * @Description: 分页查询工作任务信息
	 * @param bean
	 * @return ResultBean<PageResult<BizWorkTask>> 返回类型
	 * @throws
	 */
	@PostMapping("/pageList")
    @ApiOperation(value = "分页查询工作任务信息")
    @ControllerMonitor(description = "分页查询工作任务信息")
	public ResultBean<PageInfo> pageList(@RequestBody ReqBean<BaseQuery> bean){
		return new ResultBean<PageInfo>(workTaskService.pageList(bean));
	}
}