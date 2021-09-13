package com.gov.wiki.wechat.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gov.wiki.common.entity.wechat.WxOperationRecordResult;
import com.gov.wiki.common.enums.MaterialEnum;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.wechat.excel.ExportRecordExcel;
import com.gov.wiki.wechat.req.WxOperationRecordReq;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.gov.wiki.aspect.ControllerMonitor;
import com.gov.wiki.business.req.AuditReq;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.buss.BizSubjectQaRelationship;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.DateUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SessionUser;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.config.WxConfig;
import com.gov.wiki.organization.controller.LoginController;
import com.gov.wiki.wechat.req.NextQuestionReq;
import com.gov.wiki.wechat.req.OperationReq;
import com.gov.wiki.wechat.req.OperationResultSubmitReq;
import com.gov.wiki.wechat.req.query.OperationQuery;
import com.gov.wiki.wechat.res.NextQuestionRes;
import com.gov.wiki.wechat.service.WxMemberService;
import com.gov.wiki.wechat.service.WxOperationRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

/**
 * @ClassName: OperationController
 * @Description: 用户操作管理控制器
 * @author cys
 * @date 2020年12月15日
 */
@RestController
@RequestMapping(value = "/Wxoperation")
@Api(tags = "操作管理")
@Slf4j
public class OperationController extends BaseController {
	@Autowired
	private WxOperationRecordService operationRecordService;

	@Autowired
	private ISubjectService subjectService;

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private RedisManager redisManager;

	@Autowired
	private WxConfig wxConfig;

	@Autowired
	private WxMemberService wxMemberService;

	/**
	 * @Title: findNextQuestion @Description: 根据主题信息查询下一题 @param bean @return
	 * ResultBean<NextQuestionRes> 返回类型 @throws
	 */
	@PostMapping(value = "/findNextQuestion")
	@ApiOperation(value = "根据主题信息查询下一题")
	@ControllerMonitor(description = "根据主题信息查询下一题", operType = 1)
	public ResultBean<NextQuestionRes> findNextQuestion(@RequestBody ReqBean<NextQuestionReq> bean) {
		NextQuestionReq body = bean.getBody();
		CheckClass.check(body);
		NextQuestionRes res = new NextQuestionRes();
		res.setSubjectId(body.getSubjectId());
		if (StringUtils.isNotBlank(body.getLastQaRelationShipId())) {
			List<BizSubjectQaRelationship> resList = subjectService.findRelationShip(body.getLastQaRelationShipId());
			for (int i = resList.size() - 1; i >= 0; i--) {
				BizSubjectQaRelationship s = resList.get(i);
				if (!s.getPreOpinionId().equals(body.getOpinionId())) {
					resList.remove(i);
				}
			}
			res.setShips(resList);
		} else {
			BizSubject subject = subjectService.findById(body.getSubjectId());
			CheckUtil.notNull(subject, ResultCode.DATA_NOT_EXIST, "主题");
			res.setShips(BeanUtils.listCopy(subject.getRelationShips(), BizSubjectQaRelationship.class));
		}
		return new ResultBean<>(res);
	}

	/**
	 * @Title: addRecord @Description: 新增用户操作记录 @param bean @return
	 * ResultBean<WxOperationRecord> 返回类型 @throws
	 */
	@PostMapping(value = "/addRecord")
	@ApiOperation(value = "新增用户操作记录")
	@ControllerMonitor(description = "新增用户操作记录", operType = 2)
	public ResultBean<WxOperationRecord> addRecord(@RequestBody ReqBean<OperationReq> bean) {
		WxOperationRecord record = operationRecordService.addRecord(bean.getBody());
		
		return new ResultBean<WxOperationRecord>(record);
	}

	/**
	 * @Title: findRecordById @Description: 根据ID查询用户操作记录 @param bean @return
	 * ResultBean<WxOperationRecord> 返回类型 @throws
	 */
	@PostMapping(value = "/findRecordById")
	@ApiOperation(value = "根据ID查询用户操作记录")
	@ControllerMonitor(description = "根据ID查询用户操作记录")
	public ResultBean<WxOperationRecord> findRecordById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "请求参数不存在");
		return new ResultBean<WxOperationRecord>(operationRecordService.findById(id));
	}

	/**
	 * @Title: deleteoperation @Description: 导出材料清单excel @param [bean] @return
	 * com.gov.wiki.common.beans.ResultBean<java.lang.String> 返回类型 @throws
	 */
	@GetMapping(value = "/exportRecord")
	@ApiOperation(value = "导出材料清单excel")
	public void exportRecord(@PathParam("id") String id, HttpServletResponse response, HttpServletRequest request) {
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "请求参数不存在");
		WxOperationRecord record = operationRecordService.findById(id);
		List<ExportRecordExcel> execlData = new ArrayList<>();
		String[] acceptType = new String[] { "收原件", "验原件收复印件", "收原件及复印件", "收复印件" };
		if (CollectionUtils.isNotEmpty(record.getDetailList())) {
			List<WxOperationRecordResult> detailList = record.getDetailList();
			for (int i = 0; i < detailList.size(); i++) {
				WxOperationRecordResult data = record.getDetailList().get(i);
				ExportRecordExcel recordExcel = new ExportRecordExcel();
				recordExcel.setMaterialName(data.getMaterial().getMaterialName());
				String necessityStr = data.getNecessity() ? "必要" : "非必要";
				recordExcel.setNecessitystr(necessityStr);
				recordExcel.setMaterialType(
						MaterialEnum.MaterialType.getEnumByKey(data.getMaterial().getMaterialType()).getValue());
				recordExcel.setCheckAccept(acceptType[Integer.parseInt(data.getCheckAccept()) - 1]);
				recordExcel.setQty(data.getQty());
				recordExcel.setSort(i + 1);
				recordExcel.setRemark(data.getRemark());
				execlData.add(recordExcel);
			}
			try {
				exportExcel(response, ExportRecordExcel.class, execlData, "材料清单导出");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: deleteoperation @Description: 清除人员操作记录 @param bean @return
	 * ResultBean<String> 返回类型 @throws
	 */
	@PostMapping(value = "/deleteoperation")
	@ApiOperation(value = "清除人员操作记录")
	@ControllerMonitor(description = "清除人员操作记录", operType = 4)
	public ResultBean<String> deleteoperation(@RequestBody ReqBean<String> bean) {
		operationRecordService.deleteBymemberId(bean.getBody());
		return new ResultBean<>("删除成功");
	}

	/**
	 * @Title: deletebyid @Description: 根据ID删除记录 @param bean @return
	 * ResultBean<String> 返回类型 @throws
	 */
	@PostMapping(value = "/deletebyid")
	@ApiOperation(value = "根据ID删除记录")
	@ControllerMonitor(description = "根据ID删除记录", operType = 4)
	public ResultBean<String> deletebyid(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "请求参数不存在");
		operationRecordService.batchDelete(ids);
		return new ResultBean<>("删除成功");
	}

	/**
	 * @Title: pageList @Description: 分页查询用户操作记录 @param bean @return
	 * ResultBean<Page<WxOperationRecord>> 返回类型 @throws
	 */
	@PostMapping(value = "/pageList")
	@ApiOperation(value = "分页查询用户操作记录信息")
	@ControllerMonitor(description = "分页查询用户操作记录信息", operType = 1)
	public ResultBean<Page<WxOperationRecord>> pageList(@RequestBody ReqBean<OperationQuery> bean) {
		return new ResultBean<>(operationRecordService.pageList(bean));
	}

	@PostMapping(value = "/count")
	@ApiOperation(value = "根据条件查询数量")
	@ControllerMonitor(description = "根据条件查询数量", operType = 1)
	public ResultBean<Long> count(@RequestBody ReqBean<OperationQuery> bean) {
		return new ResultBean<>(operationRecordService.count(bean.getBody()));
	}

	/**
	 * @Title: submitOperationResult @Description: 提交用户操作记录结果信息 @param bean @return
	 * ResultBean<String> 返回类型 @throws
	 */
	@PostMapping(value = "/submitOperationResult")
	@ApiOperation(value = "提交用户操作记录结果信息")
	@ControllerMonitor(description = "提交用户操作记录结果信息", operType = 3)
	public ResultBean<String> submitOperationResult(@RequestBody ReqBean<OperationResultSubmitReq> bean) {
		operationRecordService.submitOperationResult(bean.getBody());
		return new ResultBean<String>();
	}

	/**
	 * @Title: auditOperationRecord @Description: 审核用户操作结果记录信息 @param bean @return
	 * ResultBean<String> 返回类型 @throws
	 */
	@PostMapping(value = "/auditOperationRecord")
	@ApiOperation(value = "审核用户操作结果记录信息")
	@ControllerMonitor(description = "审核用户操作结果记录信息", operType = 3)
	public ResultBean<String> auditOperationRecord(@RequestBody ReqBean<AuditReq> bean) {
		operationRecordService.auditOperationRecord(bean.getBody());
		WxOperationRecord record = operationRecordService.findById(bean.getBody().getId());
		WxMember member = wxMemberService.findById(record.getCreateBy());
		sendTemplet(bean, record, member);
		return new ResultBean<String>();
	}

	private void sendTemplet(ReqBean<AuditReq> bean, WxOperationRecord record, WxMember member) {
		
		// 发送模板消息
		WxMpTemplateMessage message = new WxMpTemplateMessage();
		message.setTemplateId(wxConfig.getTempletResult());
		List<WxMpTemplateData> list = new ArrayList<WxMpTemplateData>();
		list.add(new WxMpTemplateData("first", "你好，你的申请已"+getStatstus(bean.getBody().getStatus())));
		list.add(new WxMpTemplateData("keyword1", record.getSubject().getName()));
		list.add(new WxMpTemplateData("keyword2", getStatstus(bean.getBody().getStatus()), getColor(bean.getBody().getStatus())));
		list.add(new WxMpTemplateData("remark", record.getReason()));
		message.setUrl(wxConfig.getServerurl() + "#/record");
		message.setToUser(member.getOpenid());
		message.setData(list);
		doSend(message);
	}

	private void doSend(WxMpTemplateMessage message) {
		if(!LoginController.needUserInfo) return;
		try {
			wxMpService.getTemplateMsgService().sendTemplateMsg(message);
		} catch (WxErrorException e) {
			log.error("消息发送失败", e);
		}
	}

	private String getColor(Integer status) {
		if(status == 2) return "#ff4949";
		else if(status == 1) return "#00b23f";
		return null;
	}

	private String getStatstus(Integer status) {
		if(status == 0) return "待审核";
		else if(status == 1) return "通过";
		else if(status == 2) return "被拒绝";
		return "";
	}
	
	
	
	@PostMapping(value = "/preliminaryExaminationOperation")
	@ApiOperation(value = "创建预审")
	@ControllerMonitor(description = "新建用户操作记录", operType = 3)
	public ResultBean<WxOperationRecord> preliminaryExaminationOperation(@RequestBody ReqBean<OperationReq> bean) {
		OperationReq body = bean.getBody();
		WxOperationRecord record = operationRecordService.preliminaryExaminationOperation(body);
		if(bean.getBody().getHasSubmit()) {
			sendSubmitMessage(record);
		}
		return new ResultBean<>(record);
	}
	@PostMapping(value = "/serviceToAudit")
	@ApiOperation(value = "服务转预审")
	@ControllerMonitor(description = "服务转预审", operType = 3)
	public ResultBean<WxOperationRecord> serviceToAudit(@RequestBody ReqBean<String> bean) {
		return new ResultBean<>(operationRecordService.serviceToAudit(bean.getBody()));
	}

	@PostMapping(value = "/submitOperationRecord")
	@ApiOperation(value = "提交审核")
	public ResultBean<WxOperationRecord> submitOperationRecord(@RequestBody ReqBean<String> bean) {
		CheckUtil.notEmpty(bean.getBody(), ResultCode.COMMON_ERROR,"Id不能为空");
		WxOperationRecord record = operationRecordService.submitOperationRecord(bean.getBody());
		sendSubmitMessage(record);
		return new ResultBean<>(record);
	}

	private void sendSubmitMessage(WxOperationRecord record) {
		String token = JwtUtil.getToken();
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		// 发送模板消息
		WxMpTemplateMessage message = new WxMpTemplateMessage();
		message.setTemplateId(wxConfig.getTempletSubmit());
		List<WxMpTemplateData> list = new ArrayList<WxMpTemplateData>();
		list.add(new WxMpTemplateData("first", "你好，你申请的预审待审核。"));
		list.add(new WxMpTemplateData("keyword1", user.getMember().getRealName()));
		list.add(new WxMpTemplateData("keyword2", record.getSubject().getName()));
		list.add(new WxMpTemplateData("keyword3", DateUtil.getCurrentDateStr("yyyy-MM-dd")));
		list.add(new WxMpTemplateData("keyword4", "待审核"));
		list.add(new WxMpTemplateData("remark", "请点击即可查看详情。"));
		message.setUrl(wxConfig.getServerurl() + "#/recordDetail?id=" + record.getId());
		message.setToUser(user.getMember().getOpenId());
		message.setData(list);
		doSend(message);
	}


}