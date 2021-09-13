package com.gov.wiki.wechat.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.wechat.WxOperationRecordAudit;
import com.gov.wiki.common.utils.*;
import com.gov.wiki.wechat.dao.WxOperationRecordAuditDao;
import com.gov.wiki.wechat.req.WxOperationRecordReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.req.AuditReq;
import com.gov.wiki.business.service.ISubjectService;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.buss.vo.SimpleSubjectRes;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizOpinionMaterial;
import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.entity.wechat.WxOperationRecordResult;
import com.gov.wiki.common.enums.OperationEnum;
import com.gov.wiki.common.enums.OperationEnum.OperationStatus;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.wechat.dao.WxOperationRecordDao;
import com.gov.wiki.wechat.dao.WxOperationRecordResultDao;
import com.gov.wiki.wechat.req.OperationReq;
import com.gov.wiki.wechat.req.OperationResultSubmitReq;
import com.gov.wiki.wechat.req.query.OperationQuery;
import com.gov.wiki.wechat.service.WxOperationRecordCountService;
import com.gov.wiki.wechat.service.WxOperationRecordService;

@Service
public class WxOperationRecordServiceImpl extends BaseServiceImpl<WxOperationRecord, String, WxOperationRecordDao> implements WxOperationRecordService {
    
	@Autowired
	private WxOperationRecordCountService operationRecordCountService;
	@Autowired
	private ISubjectService subjectService;
	@Autowired
	private WxOperationRecordResultDao wxOperationRecordResultDao;
	@Autowired
	private WxOperationRecordAuditDao wxOperationRecordAuditDao;
	
	@Autowired
	private RedisManager redisManager;
	
	@Override
    public List<WxOperationRecord> findByMemberId(String memberId) {
        return this.baseRepository.findByMemberId(memberId);
    }
	
	
	

    @Override
    public Page<WxOperationRecord> findAll(Specification specification, Pageable pageable) {
        return this.baseRepository.findAll(specification, pageable);
    }

    @Override
    public void deleteBymemberId(String memberId) {
    	CheckUtil.notEmpty(memberId, ResultCode.COMMON_ERROR, "请求参数不存在");
    	List<String> ids = this.baseRepository.queryIdsByMemberId(memberId);
    	if(ids != null) {
    		this.batchDelete(ids);
    	}
    }

	@Override
	public void batchDelete(List<String> ids) {
		super.batchDelete(ids);
		wxOperationRecordResultDao.delByRecordIds(ids);
	}

	@Override
	public WxOperationRecord addRecord(OperationReq req) {
		WxOperationRecord wxOperationRecord = new WxOperationRecord();
		wxOperationRecord.setChangeAudit(0);
		wxOperationRecord.setRecordType(1);
		wxOperationRecord.setStatus(OperationStatus.submit.getKey());
		return doAddRecord(req, wxOperationRecord);
	}
	
	@Override
	public WxOperationRecord findById(String id) {
		WxOperationRecord wor = super.findById(id);
		wor.setDetailList(wxOperationRecordResultDao.findByRecordIdOrderBySortNoAsc(id));
		wor.setSubject(subjectService.findSimpleById(wor.getSubjectId()));
		wor.setWxOperationRecordAuditList(wxOperationRecordAuditDao.findByRecordIdOrderByCreateTimeAsc(id));
		return wor;
	}

	private List<BizOpinionMaterial> sort(List<BizOpinionMaterial> materials) {
		List<BizOpinionMaterial> ress = new ArrayList<BizOpinionMaterial>();
		for (BizOpinionMaterial o : materials) {
			BizOpinionMaterial r = findByObj(ress, o);
			if (r == null)
				ress.add(o);
			else {
				r.addQty(o.getQty());
			}
		}
		return ress;
	}

	private BizOpinionMaterial findByObj(List<BizOpinionMaterial> ress, BizOpinionMaterial o) {
		if (!ress.contains(o))
			return null;
		for (BizOpinionMaterial r : ress) {
			if (r.equals(o))
				return r;
		}
		return null;
	}
	
	
	private PredicateBuilder<WxOperationRecord> createBuilder(OperationQuery query){
		PredicateBuilder<WxOperationRecord> builder = Specifications.and();
		if(query.getStatusList() != null && !query.getStatusList().isEmpty()) {
			builder.in("status", query.getStatusList());
		}
		if(StringUtils.isNotBlank(query.getMemberId())) {
			builder.eq("memberId", query.getMemberId());
		}
		if(query.getRecordType()!=null){
			builder.eq("recordType", query.getRecordType());
		}
		return builder;
	}

	@Override
	public Page<WxOperationRecord> pageList(ReqBean<OperationQuery> bean) {
		OperationQuery query = bean.getBody() == null?new OperationQuery():bean.getBody();
		PredicateBuilder<WxOperationRecord> builder = createBuilder(query);
		Page<WxOperationRecord> pageList = this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
		List<WxOperationRecord> list = pageList.getContent();
		if(list != null && !list.isEmpty()) {
			Map<String, SimpleSubjectRes> subjectMap = new HashMap<String, SimpleSubjectRes>();
			SimpleSubjectRes subject = null;
			for(WxOperationRecord wor:list) {
				wor.setWxOperationRecordAuditList(wxOperationRecordAuditDao.findByRecordIdOrderByCreateTimeAsc(wor.getId()));
				String subjectId = wor.getSubjectId();
				if(StringUtils.isNotBlank(subjectId)) {
					if(subjectMap.containsKey(subjectId)) {
						wor.setSubject(subjectMap.get(subjectId));
					}else {
						subject = subjectService.findSimpleById(subjectId);
						if(subject != null) {
							wor.setSubject(subject);
							subjectMap.put(subjectId, subject);
						}
					}
				}
			}
		}
		return pageList;
	}

	@Override
	public void submitOperationResult(OperationResultSubmitReq req) {
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		if(req.getOperType() == 1) {
			WxOperationRecord wor = this.findById(req.getObjectId());
			CheckUtil.notNull(wor, ResultCode.COMMON_ERROR, "待修改操作记录不存在");
			wor.setMaterialAnnex(req.getAnnex());
			this.baseRepository.save(wor);
		}else if(req.getOperType() == 2){
			WxOperationRecordResult worr = null;
			Optional<WxOperationRecordResult> op = wxOperationRecordResultDao.findById(req.getObjectId());
			if(op.isPresent()) {
				worr = op.get();
			}
			CheckUtil.notNull(worr, ResultCode.COMMON_ERROR, "待修改操作记录结果详情不存在");
			worr.setResultAnnex(req.getAnnex());
			wxOperationRecordResultDao.save(worr);
			if(StringUtils.isNotBlank(worr.getRecordId())) {
				WxOperationRecord wor = this.findById(worr.getRecordId());
				if(wor != null) {
					this.baseRepository.save(wor);
				}
			}
		}else {
			CheckUtil.check(false, ResultCode.COMMON_ERROR, "操作对象类型参数错误");
		}
	}

	@Override
	public void auditOperationRecord(AuditReq req) {
		String userId = JwtUtil.getUserId();
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		WxOperationRecord wor = this.findById(req.getId());
		WxOperationRecordAudit audit=new WxOperationRecordAudit();
		CheckUtil.notNull(wor, ResultCode.COMMON_ERROR, "待审核操作记录不存在");
		if (wor.getStatus()==OperationEnum.OperationStatus.submit.getKey()){
			if(req.getStatus() == 1) {
				wor.setAuditStatus(OperationEnum.OperationStatus.pass.getKey());
				wor.setStatus(OperationEnum.OperationStatus.pass.getKey());
				audit.setStatus(OperationEnum.OperationStatus.pass.getKey());
			}else if(req.getStatus() == 2) {
				wor.setAuditStatus(OperationEnum.OperationStatus.refuse.getKey());
				wor.setStatus(OperationEnum.OperationStatus.refuse.getKey());
				audit.setStatus(OperationEnum.OperationStatus.refuse.getKey());
			}else {
				CheckUtil.check(false, ResultCode.COMMON_ERROR, "审核状态参数错误");
			}
			wor.setReason(req.getOpinion());
			audit.setReason(req.getOpinion());
			audit.setCreateBy(userId);
			audit.setRecordId(wor.getId());
		}else {
			CheckUtil.check(false,ResultCode.COMMON_ERROR,"当前操作记录状态不是待审状态");
		}
		this.baseRepository.save(wor);
		wxOperationRecordAuditDao.save(audit);
	}

	@Override
	public Long count(OperationQuery body) {
		PredicateBuilder<WxOperationRecord> builder = createBuilder(body);
		return this.baseRepository.count(builder.build());
	}

	@Override
	public WxOperationRecord preliminaryExaminationOperation(OperationReq req) {
		WxOperationRecordAudit audit = new WxOperationRecordAudit();
		WxOperationRecord wxOperationRecord = new WxOperationRecord();
		wxOperationRecord.setChangeAudit(0);
		wxOperationRecord.setRecordType(2);
		if(req.getHasSubmit()) {
			audit.setReason("提交审核");
			audit.setStatus(OperationEnum.OperationStatus.submit.getKey());
			wxOperationRecord.setStatus(OperationStatus.submit.getKey());
			wxOperationRecord.setAuditStatus(OperationStatus.submit.getKey());
		} else {
			audit.setReason("保存");
			audit.setStatus(OperationEnum.OperationStatus.save.getKey());
			wxOperationRecord.setStatus(OperationStatus.save.getKey());
				wxOperationRecord.setAuditStatus(OperationStatus.save.getKey());
		}
		if(StringUtils.isNotBlank(req.getServiceRecordId())) {
			WxOperationRecord bean = this.findById(req.getServiceRecordId());
			bean.setChangeAudit(1);
			this.saveOrUpdate(bean);
		}
		wxOperationRecord =  doAddRecord(req, wxOperationRecord);
		audit.setRecordId(wxOperationRecord.getId());
		this.wxOperationRecordAuditDao.save(audit);
		return wxOperationRecord;
		
	}

	private WxOperationRecord doAddRecord(OperationReq req, WxOperationRecord wxOperationRecord) {
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		BizSubject subject = subjectService.findById(req.getSubjectId());
		CheckUtil.notNull(subject, ResultCode.COMMON_ERROR, "主题不存在");
		List<BizOpinionMaterial> materials = subjectService.findMaterial(req);
		List<BizOpinionMaterial> ress = sort(materials);
		//wxOperationRecord.setDetailList(orrSet);
		wxOperationRecord.setMemberId(JwtUtil.getUserId());
		wxOperationRecord.setOptions(StringUtils.listToString(req.getOpinionIds(), ","));// 所需材料id
		wxOperationRecord.setSubjectId(subject.getId());
		wxOperationRecord = this.saveOrUpdate(wxOperationRecord);
		Set<WxOperationRecordResult> orrSet = new HashSet<WxOperationRecordResult>();
		if(ress != null && !ress.isEmpty()) {
			int sortNo = 1;
			for(BizOpinionMaterial om:ress) {
				WxOperationRecordResult orr = new WxOperationRecordResult();
				orr.setCheckAccept(om.getCheckAccept());
				orr.setDelFlag(false);
				orr.setMaterialId(om.getMaterialId());
				orr.setNecessity(om.getNecessity());
				orr.setQty(om.getQty());
				orr.setRemark(om.getRemark());
				orr.setSortNo(sortNo);
				orr.setRecordId(wxOperationRecord.getId());
				orrSet.add(orr);
				sortNo++;
			}
		}
		if(!orrSet.isEmpty()) {
			wxOperationRecordResultDao.saveAll(orrSet);
		}
		// 新增热门主题统计
		operationRecordCountService.addCountBySubjectId(1, subject.getId());
		return this.findById(wxOperationRecord.getId());
	}

	/**
	 * @Title: submitOperationRecord
	 * @Description: 提交审核资料为预审
	 * @author: DeYuan
	 * @param [body]
	 * @return  com.gov.wiki.common.entity.wechat.WxOperationRecord  返回类型
	 * @throws
	 */
	@Override
	public WxOperationRecord submitOperationRecord(String id) {
		WxOperationRecord operationRecord = this.findById(id);
		CheckUtil.notNull(operationRecord, ResultCode.DATA_NOT_EXIST, "预审材料");
		WxOperationRecordAudit audit = new WxOperationRecordAudit();
		if(operationRecord.getAuditStatus()==0 || operationRecord.getAuditStatus()==4){
			operationRecord.setAuditStatus(OperationEnum.OperationStatus.submit.getKey());
			operationRecord.setStatus(OperationEnum.OperationStatus.submit.getKey());
			audit.setRecordId(operationRecord.getId());
			audit.setReason("提交审核");
			audit.setStatus(OperationEnum.OperationStatus.submit.getKey());
		}else {
			CheckUtil.check(false,ResultCode.COMMON_ERROR,"只有保存状态，或者审核未通过才可以提交审核");
		}
		wxOperationRecordAuditDao.save(audit);
		return this.saveOrUpdate(operationRecord);
	}




	@Override
	public WxOperationRecord serviceToAudit(String body) {
		WxOperationRecord service = this.findById(body);
		service.setChangeAudit(1);
		WxOperationRecord bean = BeanUtils.copyProperties(service, WxOperationRecord.class);
		bean.setId(null);
		bean.setStatus(0);
		bean.setAuditStatus(0);
		bean.setRecordType(2);
		this.baseRepository.saveAndFlush(bean);
		this.saveOrUpdate(service);
		List<WxOperationRecordResult> detailList = service.getDetailList();
		for (WxOperationRecordResult result : detailList) {
			WxOperationRecordResult r = BeanUtils.copyProperties(result, WxOperationRecordResult.class);
			r.setId(null);
			r.setRecordId(bean.getId());
			this.wxOperationRecordResultDao.save(r);
		}
		WxOperationRecordAudit audit = new WxOperationRecordAudit();
		audit.setReason("保存材料");
		audit.setRecordId(bean.getId());
		audit.setStatus(OperationStatus.save.getKey());
		this.wxOperationRecordAuditDao.save(audit);
		return this.findById(bean.getId());
	}
}
