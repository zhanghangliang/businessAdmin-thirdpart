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
 * @Description: ???????????????????????????
 * @author cys
 * @date 2020???12???15???
 */
@RestController
@RequestMapping(value = "/Wxoperation")
@Api(tags = "????????????")
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
	 * @Title: findNextQuestion @Description: ????????????????????????????????? @param bean @return
	 * ResultBean<NextQuestionRes> ???????????? @throws
	 */
	@PostMapping(value = "/findNextQuestion")
	@ApiOperation(value = "?????????????????????????????????")
	@ControllerMonitor(description = "?????????????????????????????????", operType = 1)
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
			CheckUtil.notNull(subject, ResultCode.DATA_NOT_EXIST, "??????");
			res.setShips(BeanUtils.listCopy(subject.getRelationShips(), BizSubjectQaRelationship.class));
		}
		return new ResultBean<>(res);
	}

	/**
	 * @Title: addRecord @Description: ???????????????????????? @param bean @return
	 * ResultBean<WxOperationRecord> ???????????? @throws
	 */
	@PostMapping(value = "/addRecord")
	@ApiOperation(value = "????????????????????????")
	@ControllerMonitor(description = "????????????????????????", operType = 2)
	public ResultBean<WxOperationRecord> addRecord(@RequestBody ReqBean<OperationReq> bean) {
		WxOperationRecord record = operationRecordService.addRecord(bean.getBody());
		
		return new ResultBean<WxOperationRecord>(record);
	}

	/**
	 * @Title: findRecordById @Description: ??????ID???????????????????????? @param bean @return
	 * ResultBean<WxOperationRecord> ???????????? @throws
	 */
	@PostMapping(value = "/findRecordById")
	@ApiOperation(value = "??????ID????????????????????????")
	@ControllerMonitor(description = "??????ID????????????????????????")
	public ResultBean<WxOperationRecord> findRecordById(@RequestBody ReqBean<String> bean) {
		String id = bean.getBody();
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "?????????????????????");
		return new ResultBean<WxOperationRecord>(operationRecordService.findById(id));
	}

	/**
	 * @Title: deleteoperation @Description: ??????????????????excel @param [bean] @return
	 * com.gov.wiki.common.beans.ResultBean<java.lang.String> ???????????? @throws
	 */
	@GetMapping(value = "/exportRecord")
	@ApiOperation(value = "??????????????????excel")
	public void exportRecord(@PathParam("id") String id, HttpServletResponse response, HttpServletRequest request) {
		CheckUtil.notEmpty(id, ResultCode.COMMON_ERROR, "?????????????????????");
		WxOperationRecord record = operationRecordService.findById(id);
		List<ExportRecordExcel> execlData = new ArrayList<>();
		String[] acceptType = new String[] { "?????????", "?????????????????????", "?????????????????????", "????????????" };
		if (CollectionUtils.isNotEmpty(record.getDetailList())) {
			List<WxOperationRecordResult> detailList = record.getDetailList();
			for (int i = 0; i < detailList.size(); i++) {
				WxOperationRecordResult data = record.getDetailList().get(i);
				ExportRecordExcel recordExcel = new ExportRecordExcel();
				recordExcel.setMaterialName(data.getMaterial().getMaterialName());
				String necessityStr = data.getNecessity() ? "??????" : "?????????";
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
				exportExcel(response, ExportRecordExcel.class, execlData, "??????????????????");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: deleteoperation @Description: ???????????????????????? @param bean @return
	 * ResultBean<String> ???????????? @throws
	 */
	@PostMapping(value = "/deleteoperation")
	@ApiOperation(value = "????????????????????????")
	@ControllerMonitor(description = "????????????????????????", operType = 4)
	public ResultBean<String> deleteoperation(@RequestBody ReqBean<String> bean) {
		operationRecordService.deleteBymemberId(bean.getBody());
		return new ResultBean<>("????????????");
	}

	/**
	 * @Title: deletebyid @Description: ??????ID???????????? @param bean @return
	 * ResultBean<String> ???????????? @throws
	 */
	@PostMapping(value = "/deletebyid")
	@ApiOperation(value = "??????ID????????????")
	@ControllerMonitor(description = "??????ID????????????", operType = 4)
	public ResultBean<String> deletebyid(@RequestBody ReqBean<List<String>> bean) {
		List<String> ids = bean.getBody();
		CheckUtil.check(ids != null && !ids.isEmpty(), ResultCode.COMMON_ERROR, "?????????????????????");
		operationRecordService.batchDelete(ids);
		return new ResultBean<>("????????????");
	}

	/**
	 * @Title: pageList @Description: ?????????????????????????????? @param bean @return
	 * ResultBean<Page<WxOperationRecord>> ???????????? @throws
	 */
	@PostMapping(value = "/pageList")
	@ApiOperation(value = "????????????????????????????????????")
	@ControllerMonitor(description = "????????????????????????????????????", operType = 1)
	public ResultBean<Page<WxOperationRecord>> pageList(@RequestBody ReqBean<OperationQuery> bean) {
		return new ResultBean<>(operationRecordService.pageList(bean));
	}

	@PostMapping(value = "/count")
	@ApiOperation(value = "????????????????????????")
	@ControllerMonitor(description = "????????????????????????", operType = 1)
	public ResultBean<Long> count(@RequestBody ReqBean<OperationQuery> bean) {
		return new ResultBean<>(operationRecordService.count(bean.getBody()));
	}

	/**
	 * @Title: submitOperationResult @Description: ???????????????????????????????????? @param bean @return
	 * ResultBean<String> ???????????? @throws
	 */
	@PostMapping(value = "/submitOperationResult")
	@ApiOperation(value = "????????????????????????????????????")
	@ControllerMonitor(description = "????????????????????????????????????", operType = 3)
	public ResultBean<String> submitOperationResult(@RequestBody ReqBean<OperationResultSubmitReq> bean) {
		operationRecordService.submitOperationResult(bean.getBody());
		return new ResultBean<String>();
	}

	/**
	 * @Title: auditOperationRecord @Description: ???????????????????????????????????? @param bean @return
	 * ResultBean<String> ???????????? @throws
	 */
	@PostMapping(value = "/auditOperationRecord")
	@ApiOperation(value = "????????????????????????????????????")
	@ControllerMonitor(description = "????????????????????????????????????", operType = 3)
	public ResultBean<String> auditOperationRecord(@RequestBody ReqBean<AuditReq> bean) {
		operationRecordService.auditOperationRecord(bean.getBody());
		WxOperationRecord record = operationRecordService.findById(bean.getBody().getId());
		WxMember member = wxMemberService.findById(record.getCreateBy());
		sendTemplet(bean, record, member);
		return new ResultBean<String>();
	}

	private void sendTemplet(ReqBean<AuditReq> bean, WxOperationRecord record, WxMember member) {
		
		// ??????????????????
		WxMpTemplateMessage message = new WxMpTemplateMessage();
		message.setTemplateId(wxConfig.getTempletResult());
		List<WxMpTemplateData> list = new ArrayList<WxMpTemplateData>();
		list.add(new WxMpTemplateData("first", "????????????????????????"+getStatstus(bean.getBody().getStatus())));
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
			log.error("??????????????????", e);
		}
	}

	private String getColor(Integer status) {
		if(status == 2) return "#ff4949";
		else if(status == 1) return "#00b23f";
		return null;
	}

	private String getStatstus(Integer status) {
		if(status == 0) return "?????????";
		else if(status == 1) return "??????";
		else if(status == 2) return "?????????";
		return "";
	}
	
	
	
	@PostMapping(value = "/preliminaryExaminationOperation")
	@ApiOperation(value = "????????????")
	@ControllerMonitor(description = "????????????????????????", operType = 3)
	public ResultBean<WxOperationRecord> preliminaryExaminationOperation(@RequestBody ReqBean<OperationReq> bean) {
		OperationReq body = bean.getBody();
		WxOperationRecord record = operationRecordService.preliminaryExaminationOperation(body);
		if(bean.getBody().getHasSubmit()) {
			sendSubmitMessage(record);
		}
		return new ResultBean<>(record);
	}
	@PostMapping(value = "/serviceToAudit")
	@ApiOperation(value = "???????????????")
	@ControllerMonitor(description = "???????????????", operType = 3)
	public ResultBean<WxOperationRecord> serviceToAudit(@RequestBody ReqBean<String> bean) {
		return new ResultBean<>(operationRecordService.serviceToAudit(bean.getBody()));
	}

	@PostMapping(value = "/submitOperationRecord")
	@ApiOperation(value = "????????????")
	public ResultBean<WxOperationRecord> submitOperationRecord(@RequestBody ReqBean<String> bean) {
		CheckUtil.notEmpty(bean.getBody(), ResultCode.COMMON_ERROR,"Id????????????");
		WxOperationRecord record = operationRecordService.submitOperationRecord(bean.getBody());
		sendSubmitMessage(record);
		return new ResultBean<>(record);
	}

	private void sendSubmitMessage(WxOperationRecord record) {
		String token = JwtUtil.getToken();
		SessionUser user = (SessionUser) redisManager.getSessionUser(token);
		// ??????????????????
		WxMpTemplateMessage message = new WxMpTemplateMessage();
		message.setTemplateId(wxConfig.getTempletSubmit());
		List<WxMpTemplateData> list = new ArrayList<WxMpTemplateData>();
		list.add(new WxMpTemplateData("first", "???????????????????????????????????????"));
		list.add(new WxMpTemplateData("keyword1", user.getMember().getRealName()));
		list.add(new WxMpTemplateData("keyword2", record.getSubject().getName()));
		list.add(new WxMpTemplateData("keyword3", DateUtil.getCurrentDateStr("yyyy-MM-dd")));
		list.add(new WxMpTemplateData("keyword4", "?????????"));
		list.add(new WxMpTemplateData("remark", "??????????????????????????????"));
		message.setUrl(wxConfig.getServerurl() + "#/recordDetail?id=" + record.getId());
		message.setToUser(user.getMember().getOpenId());
		message.setData(list);
		doSend(message);
	}


}