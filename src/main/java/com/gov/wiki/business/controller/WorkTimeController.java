package com.gov.wiki.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.WorkTaskReq;
import com.gov.wiki.business.req.WorkTimeReq;
import com.gov.wiki.business.req.query.WorkTimeQuery;
import com.gov.wiki.business.service.BizWorkTimeService;
import com.gov.wiki.business.service.IWorkTaskService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizWorkTask;
import com.gov.wiki.common.entity.buss.BizWorkTime;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/workTime")
@RestController
@Api(tags = "工作日历")
public class WorkTimeController {

	
	@Autowired
	private  BizWorkTimeService workTimeService;
	
	@PostMapping("/update")
    @ApiOperation(value = "更新工作日历")
    @ControllerMonitor(description = "更新工作日历", operType = 8)
	public ResultBean<BizWorkTime> update(@RequestBody ReqBean<WorkTimeReq> bean){
		WorkTimeReq req = bean.getBody();
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		BizWorkTime task = BeanUtils.copyProperties(req, BizWorkTime.class);
		task = workTimeService.saveOrUpdate(task);
		return new ResultBean<BizWorkTime>(task);
	}
	
	
	@PostMapping("/queryList")
	@ApiOperation(value = "根据条件查询工作日历")
	@ControllerMonitor(description = "更新工作日历", operType = 8)
	public ResultBean<List<BizWorkTime>> list(@RequestBody ReqBean<WorkTimeQuery> bean){
		WorkTimeQuery req = bean.getBody();
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		return new ResultBean<List<BizWorkTime>>(workTimeService.list(req));
	}
}
