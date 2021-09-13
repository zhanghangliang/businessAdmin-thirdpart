package com.gov.wiki.business.controller;

import static com.gov.wiki.common.utils.CheckUtil.check;
import java.util.List;
import com.gov.wiki.business.res.SubjectAuditBasicRes;
import com.gov.wiki.business.res.SubjectWxRes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.AuditReq;
import com.gov.wiki.business.req.DataDelReq;
import com.gov.wiki.business.req.SubjectAuditReq;
import com.gov.wiki.business.req.SubjectRes;
import com.gov.wiki.business.req.query.SubjectQuery;
import com.gov.wiki.business.service.ISubjectAuditService;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.buss.BizSubjectAudit;
import com.gov.wiki.common.res.PageResult;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.ReqBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ClassName: SubjectController
 * @Description: 主题管理控制器
 * @author cys
 * @date 2020年11月24日
 */
@RequestMapping("/Subject")
@RestController
@Api(tags = "主题管理")
public class SubjectController {
    
	@Autowired
	private ISubjectService subjectService;
	@Autowired
	private ISubjectAuditService subjectAuditService;
	
	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或修改主题审核记录
	 * @param bean
	 * @return ResultBean<SubjectRes> 返回类型
	 * @throws
	 */
	@PostMapping("/saveOrUpdate")
	@ApiOperation(value = "新增或修改主题操作记录")
	@ControllerMonitor(description = "新增或修改主题操作记录", operType = 2)
	public ResultBean<SubjectRes> saveOrUpdate(@RequestBody ReqBean<SubjectAuditReq> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		BizSubjectAudit audit = subjectAuditService.saveOrUpdate(bean.getBody());
		return new ResultBean<SubjectRes>(BeanUtils.copyProperties(audit, SubjectRes.class));
	}
	
	/**
	 * @Title: audit
	 * @Description: 审核主题操作记录信息
	 * @param bean
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	@PostMapping("/audit")
	@ApiOperation(value = "审核主题操作记录信息")
	@ControllerMonitor(description = "审核主题操作记录信息", operType = 3)
	public ResultBean<String> audit(@RequestBody ReqBean<AuditReq> bean) {
		subjectAuditService.audit(bean.getBody());
		return new ResultBean<String>();
	}
	
	/**
	 * @Title: pageAudit
	 * @Description: 分页查询主题审核库信息
	 * @param bean
	 * @return ResultBean<PageResult<SubjectRes>> 返回类型
	 * @throws
	 */
	@PostMapping("/pageAudit")
	@ApiOperation(value = "分页查询主题审核库信息")
	@ControllerMonitor(description = "分页查询主题审核库信息", operType = 1)
	public ResultBean<PageResult<SubjectRes>> pageAudit(@RequestBody ReqBean<SubjectQuery> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		Page<BizSubjectAudit> page = subjectAuditService.page(bean);
		return new ResultBean<>(BeanUtils.pageCopy(page, SubjectRes.class));
	}

	/**
	 * @Title: pageAudit
	 * @Description: 分页查询主题审核库基础信息
	 * @param bean
	 * @return ResultBean<PageResult<SubjectRes>> 返回类型
	 * @throws
	 */
	@PostMapping("/pageBasicAudit")
	@ApiOperation(value = "分页查询主题审核库信息")
	@ControllerMonitor(description = "分页查询主题审核库信息", operType = 1)
	public ResultBean<PageResult<SubjectAuditBasicRes>> pageBasicAudit(@RequestBody ReqBean<SubjectQuery> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		return new ResultBean<>(subjectAuditService.pageBasic(bean));
	}
	
	/**
	 * @Title: page
	 * @Description: 分页查询主题数据
	 * @param bean
	 * @return ResultBean<PageResult<SubjectRes>> 返回类型
	 * @throws
	 */
	@PostMapping("/page")
	@ApiOperation(value = "分页查询主题数据")
	@ControllerMonitor(description = "分页查询主题数据", operType = 1)
	public ResultBean<PageResult<SubjectRes>> page(@RequestBody ReqBean<SubjectQuery> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		Page<BizSubject> page = subjectService.page(bean);
		return new ResultBean<PageResult<SubjectRes>>(BeanUtils.pageCopy(page, SubjectRes.class));
	}
	@PostMapping("/wxPage")
	@ApiOperation(value = "分页查询主题数据")
	@ControllerMonitor(description = "分页查询主题数据", operType = 1)
	public ResultBean<PageResult<SubjectWxRes>> wxPage(@RequestBody ReqBean<SubjectQuery> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		return new ResultBean<PageResult<SubjectWxRes>>(subjectService.wxPage(bean));
	}
	
	/**
	 * @Title: findAuditById
	 * @Description: 根据id查询主题审核库数据
	 * @param bean
	 * @return ResultBean<BizSubjectAudit> 返回类型
	 * @throws
	 */
	@PostMapping("/findAuditById")
	@ApiOperation(value = "根据id查询主题审核库数据")
	@ControllerMonitor(description = "根据id查询主题审核库数据", operType = 1)
	public ResultBean<BizSubjectAudit> findAuditById(@RequestBody ReqBean<String> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		CheckUtil.notEmpty(bean.getBody(), ResultCode.PARAM_NULL, "id");
		BizSubjectAudit audit = subjectAuditService.findById(bean.getBody());
		return new ResultBean<BizSubjectAudit>(audit);
	}
	
	/**
	 * @Title: findById
	 * @Description: 根据id查询主题库数据
	 * @param bean
	 * @return ResultBean<BizSubject> 返回类型
	 * @throws
	 */
	@PostMapping("/findById")
	@ApiOperation(value = "根据id查询主题库数据")
	@ControllerMonitor(description = "根据id查询主题库数据", operType = 1)
	public ResultBean<BizSubject> findById(@RequestBody ReqBean<String> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		CheckUtil.notEmpty(bean.getBody(), ResultCode.PARAM_NULL, "id");
		BizSubject audit = subjectService.findById(bean.getBody());
		return new ResultBean<BizSubject>(audit);
	}
	
	/**
	 * @Title: delAuditById
	 * @Description: 根据id删除主题审核库中数据
	 * @param bean
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	@PostMapping("/delAuditById")
	@ApiOperation(value = "根据id删除主题审核库中数据")
	@ControllerMonitor(description = "根据id删除主题审核库中数据", operType = 1)
	public ResultBean<String> delAuditById(@RequestBody ReqBean<List<String>> bean) {
		CheckUtil.notNull(bean, ResultCode.PARAM_NULL, "参数");
		check(bean.getBody()!= null && !bean.getBody().isEmpty(), ResultCode.PARAM_NULL, "id");
		subjectAuditService.batchDelete(bean.getBody());
		return new ResultBean<String>();
	}
	
	/**
	 * @Title: delById
	 * @Description: 根据id删除主题库数据
	 * @param bean
	 * @return ResultBean<String> 返回类型
	 * @throws
	 */
	@PostMapping("/delById")
	@ApiOperation(value = "根据id删除主题库数据")
	@ControllerMonitor(description = "根据id删除主题库数据", operType = 1)
	public ResultBean<String> delById(@RequestBody ReqBean<DataDelReq> bean) {
		subjectService.delSubject(bean.getBody());
		return new ResultBean<String>();
	}
}