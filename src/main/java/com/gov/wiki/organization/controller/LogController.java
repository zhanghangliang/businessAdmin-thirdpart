package com.gov.wiki.organization.controller;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.entity.system.OrgCustomer;
import com.gov.wiki.common.entity.system.SysLog;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.system.req.query.LogQuery;
import com.gov.wiki.system.service.ILogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: LogController
 * @Description: 日志管理控制器
 * @author cys
 * @date 2019年12月10日
 */
@RequestMapping("/log")
@RestController
@Api(tags = "日志管理")
public class LogController {

	/**
	 * 注入logService
	 */
	@Autowired
	private ILogService logService;

	@Autowired
	private RedisManager redisManager;
	
	/**
	 * @Title: delById 
	 * @Description: 根据ID删除日志信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据ID删除日志信息")
	@ControllerMonitor(description = "根据ID删除日志信息", operType = 4)
	public ResultBean delById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "参数不存在！");
		logService.deleteById(id);
		return new ResultBean();
	}

	/**
	 * @Title: batchDel
	 * @Description: 批量删除日志信息
	 * @param 设定文件 
	 * @return ResultBean    返回类型 
	 * @throws
	 */
	@PostMapping("/batchDel")
	@ApiOperation(value = "批量删除日志信息")
	@ControllerMonitor(description = "批量删除日志信息", operType = 4)
	public ResultBean batchDel(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.notNull(ids, ResultCode.COMMON_ERROR, "参数不存在！");
		logService.batchDelete(ids);
		return new ResultBean();
	}

	/**
	 * @Title: pageList 
	 * @Description: 分页查询日志信息
	 * @param 设定文件 
	 * @return ResultBean<PageInfo>    返回类型 
	 * @throws
	 */
	@PostMapping("/pageList")
	@ApiOperation(value = "分页查询日志信息")
	@ControllerMonitor(description = "分页查询日志信息")
	public ResultBean<Page<SysLog>> pageList(@RequestBody ReqBean<LogQuery> bean, HttpServletRequest request) {
		LogQuery body = bean.getBody();
		System.out.println(JSON.toJSONString(body));
		String token = request.getHeader(Constants.TOKEN);
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		Specification<SysLog> specification = Specifications.<SysLog>and()
				.eq(bean.getBody().getLogType()!=null,"logType",body.getLogType())
				.like(StringUtils.isNotBlank(bean.getBody().getKeywords()),"logContent","%"+body.getKeywords()+"%")
				.build();
		ReqHeader header = bean.getHeader();
		Sort sort = header.getSort();
		return new ResultBean<>(logService.findAll(specification, PageRequest.of(header.getPageNumber(),header.getPageSize(),sort)));
	}
}