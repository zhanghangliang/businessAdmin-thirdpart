/**
 * @Title: QuestionController.java
 * @Package com.gov.wiki.business.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.QuestionAuditAuditReq;
import com.gov.wiki.business.req.QuestionAuditReq;
import com.gov.wiki.business.req.query.QuestionQuery;
import com.gov.wiki.business.res.QuestionRes;
import com.gov.wiki.business.service.IQuestionAuditService;
import com.gov.wiki.business.service.IQuestionService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizQuestion;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: QuestionController
 * @Description: 问题管理控制器
 * @author cys
 * @date 2020年8月25日
 */
@RequestMapping("/question")
@RestController
@Api(tags = "问题管理")
public class QuestionController {

	@Autowired
	private IQuestionService questionService;

	@Autowired
	private IQuestionAuditService questionAuditService;

	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或修改问题审核记录
	 * @param bean
	 * @return ResultBean<QuestionRes> 返回类型
	 * @throws
	 */
	@PostMapping("/saveOrUpdate")
	@ApiOperation(value = "新增或修改问题审核记录")
	@ControllerMonitor(description = "新增或修改问题审核记录", operType = 2)
	public ResultBean<QuestionRes> saveOrUpdate(@RequestBody ReqBean<QuestionAuditReq> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		BizQuestionAudit audit = questionAuditService.saveOrUpdate(bean.getBody());
		audit = questionAuditService.findById(audit.getId());
		return new ResultBean<QuestionRes>(BeanUtils.copyProperties(audit, QuestionRes.class));
	}
	
	/**
	 * @Title: audit
	 * @Description: 审核问题,body：问题审核库中的id
	 * @param bean
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	@PostMapping("/audit")
	@ApiOperation(value = "审核问题,body：问题审核库中的id")
	@ControllerMonitor(description = "审核问题,body：问题审核库中的id", operType = 3)
	public ResultBean<String> audit(@RequestBody ReqBean<QuestionAuditAuditReq> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		CheckClass.check(bean.getBody());
		questionAuditService.audit(bean.getBody());
		return new ResultBean<String>();
	}
	
	@PostMapping("/pageAudit")
	@ApiOperation(value = "分页查询数据")
	@ControllerMonitor(description = "分页查询问题审核库", operType = 1)
	public ResultBean<PageInfo> pageAudit(@RequestBody ReqBean<QuestionQuery> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		return questionAuditService.pageList(bean);
	}
	@PostMapping("/page")
	@ApiOperation(value = "分页查询数据")
	@ControllerMonitor(description = "分页查询问题库", operType = 1)
	public ResultBean<PageResult<QuestionRes>> page(@RequestBody ReqBean<QuestionQuery> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		Page<BizQuestion> page = questionService.page(bean);
		PageResult<QuestionRes> result = BeanUtils.pageCopy(page, QuestionRes.class);
		result.setCurrentPage(result.getCurrentPage()+1);
		return new ResultBean<PageResult<QuestionRes>>(result);
	}
	
	@PostMapping("/findAuditById")
	@ApiOperation(value = "根据id查询问题审核库数据")
	@ControllerMonitor(description = "根据id查询问题审核库数据", operType = 1)
	public ResultBean<QuestionRes> findAuditById(@RequestBody ReqBean<String> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		CheckUtil.notEmpty(bean.getBody(), ResultCode.PARAM_NULL, "id");
		BizQuestionAudit audit = questionAuditService.findById(bean.getBody());
		return new ResultBean<QuestionRes>(BeanUtils.copyProperties(audit, QuestionRes.class));
	}
	
	@PostMapping("/findById")
	@ApiOperation(value = "根据id查询问题库数据")
	@ControllerMonitor(description = "根据id查询问题库数据", operType = 1)
	public ResultBean<QuestionRes> findById(@RequestBody ReqBean<String> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		CheckUtil.notEmpty(bean.getBody(), ResultCode.PARAM_NULL, "id");
		BizQuestion audit = questionService.findById(bean.getBody());
		return new ResultBean<QuestionRes>(BeanUtils.copyProperties(audit, QuestionRes.class));
	}
	
	@PostMapping("/delAuditById")
	@ApiOperation(value = "根据id删除问题审核库中数据")
	@ControllerMonitor(description = "根据id查询问题审核库数据", operType = 1)
	public ResultBean<String> delAuditById(@RequestBody ReqBean<List<String>> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		CheckUtil.check(bean.getBody() != null && !bean.getBody().isEmpty(), ResultCode.PARAM_NULL, "id");
		questionAuditService.batchDelete(bean.getBody());
		return new ResultBean<String>();
	}
	
	@PostMapping("/delById")
	@ApiOperation(value = "根据id删除问题库数据")
	@ControllerMonitor(description = "根据id删除问题库数据", operType = 1)
	public ResultBean<String> delById(@RequestBody ReqBean<DataDelReq> bean) {
		questionService.delQuestions(bean.getBody());
		return new ResultBean<String>();
	}
}