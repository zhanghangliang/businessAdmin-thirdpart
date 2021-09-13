package com.gov.wiki.wechat.service;

import java.util.List;

import javax.transaction.Transactional;

import com.gov.wiki.wechat.req.WxOperationRecordReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.gov.wiki.business.req.AuditReq;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.wechat.req.OperationReq;
import com.gov.wiki.wechat.req.OperationResultSubmitReq;
import com.gov.wiki.wechat.req.query.OperationQuery;

/**
 * @ClassName: WxOperationRecordService
 * @Description: 微信操作记录业务处理接口
 * @author cys
 * @date 2020年12月15日
 */
public interface WxOperationRecordService extends IBaseService<WxOperationRecord, String> {
	List<WxOperationRecord> findByMemberId(String memberId);

	Page<WxOperationRecord> findAll(Specification specification, Pageable pageable);

	void deleteBymemberId(String memberid);
	
	/**
	 * @Title: addRecord
	 * @Description: 新增操作记录
	 * @param req
	 * @return WxOperationRecord 返回类型
	 * @throws
	 */
	@Transactional
	WxOperationRecord addRecord(OperationReq req);
	
	/**
	 * @Title: pageList
	 * @Description: 分页查询用户操作记录信息
	 * @param bean
	 * @return Page<WxOperationRecord> 返回类型
	 * @throws
	 */
	Page<WxOperationRecord> pageList(ReqBean<OperationQuery> bean);
	
	/**
	 * @Title: submitOperationResult
	 * @Description: 提交用户操作结果信息
	 * @param req
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	void submitOperationResult(OperationResultSubmitReq req);
	
	/**
	 * @Title: auditOperationRecord
	 * @Description: 审核用户操作结果记录信息
	 * @param req
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	void auditOperationRecord(AuditReq req);

	/**
	 * 根据条件查询数量
	 * @param body
	 * @return
	 */
	Long count(OperationQuery body);

	/**
	 * 新建用户操作记录
	 * @param body
	 * @return
	 */
	WxOperationRecord preliminaryExaminationOperation(OperationReq body);

	/**
	 * 提交预审材料
	 * @param body
	 * @return
	 */
	WxOperationRecord submitOperationRecord(String body);

	WxOperationRecord serviceToAudit(String body);


}