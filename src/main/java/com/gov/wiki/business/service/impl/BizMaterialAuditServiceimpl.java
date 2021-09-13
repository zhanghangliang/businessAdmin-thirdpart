package com.gov.wiki.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizMaterialAuditDao;
import com.gov.wiki.business.req.MaterialQuery;
import com.gov.wiki.business.req.MaterialReq;
import com.gov.wiki.business.req.query.MaterialExistQuery;
import com.gov.wiki.business.res.MaterialExistRes;
import com.gov.wiki.business.service.BizMaterialAuditService;
import com.gov.wiki.business.service.BizMaterialDepositoryService;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.system.service.IFileService;

@Service
public class BizMaterialAuditServiceimpl extends BaseServiceImpl<BizMaterialAudit,String, BizMaterialAuditDao> implements BizMaterialAuditService {
    
	@Autowired
    private IFileService fileService;
	@Autowired
	private BizMaterialDepositoryService bizMaterialDepositoryService;
	
	@Override
    public ResultBean<List<String>> findAllAuditMaterialType(List<String> creater) {
        return new ResultBean<>(this.baseRepository.findAllAuditMaterialType(creater));
    }

    @Override
    public Page<BizMaterialAudit> findAll(Specification specification, PageRequest pageRequest) {
        return this.baseRepository.findAll(specification,pageRequest);
    }

	@Override
	public long findCountByMaterialId(String materialId) {
		BizMaterialAudit bean = new BizMaterialAudit();
		bean.setMaterialId(materialId);
		bean.setAuditState(0);
		Example<BizMaterialAudit> example = Example.of(bean);
		return this.baseRepository.count(example);
	}

	@Override
	public Page<BizMaterialAudit> pageList(ReqBean<MaterialQuery> bean) {
		String userId = JwtUtil.getUserId();
		CheckUtil.notEmpty(userId, ResultCode.COMMON_ERROR, "用户未登录！");
		MaterialQuery query = bean.getBody() == null?new MaterialQuery():bean.getBody();
		PredicateBuilder<BizMaterialAudit> builder = Specifications.and();
		//builder.eq("createBy", userId);
		if(StringUtils.isNotBlank(query.getKeywords())) {
			builder.predicate(Specifications.or()
				.like("materialName", "%" + query.getKeywords() + "%")
				.like("version", "%" + query.getKeywords() + "%")
				.build());
		}
		if(query.getOperTypeList() != null && !query.getOperTypeList().isEmpty()) {
			builder.in("operationStatus", query.getOperTypeList());
		}
		if(query.getStatusList() != null && !query.getStatusList().isEmpty()) {
			builder.in("auditState", query.getStatusList());
		}
		if(StringUtils.isNotBlank(query.getMaterialSource())) {
			builder.eq("materialSource", query.getMaterialSource());
		}
		if(StringUtils.isNotBlank(query.getDutyType())) {
			builder.eq("dutyType", query.getDutyType());
		}
		if(query.getTypeList() != null && !query.getTypeList().isEmpty()) {
			builder.in("materialType", query.getTypeList());
		}
		return this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
	}

	@Override
	public BizMaterialAudit saveOrUpdate(MaterialReq req) {
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckClass.check(req);
		BizMaterialAudit old = null;
		if(StringUtils.isNotBlank(req.getId())) {
			old = this.findById(req.getId());
		}
		BizMaterialAudit entity = req.toEntity();
		int modifyTimes = 1;
		if(old != null) {
			CheckUtil.check(old.getAuditState() == null || old.getAuditState().intValue() != 2, ResultCode.COMMON_ERROR, "无法修改已通过的数据");
			entity.setVersion(old.getVersion());
			entity.setModifyTimes(old.getModifyTimes());
			entity.setId(old.getId());
		}else {
			if(StringUtils.isNotBlank(entity.getMaterialDepositoryId())) {
				BizMaterialDepository bmd = bizMaterialDepositoryService.findById(entity.getMaterialDepositoryId());
				if(bmd != null && bmd.getModifyTimes() != null) {
					modifyTimes += bmd.getModifyTimes();
				}
			}
			entity.setModifyTimes(modifyTimes);
			entity.setVersion("V" + modifyTimes + ".0");
		}
		entity.setAuditState(0);
		entity = this.saveOrUpdate(entity);
	    List<String> fileIds = req.getSysFile();
	    List<SysFile> sysFiles = new ArrayList<>();
	    if(fileIds != null && !fileIds.isEmpty()){
	    	for (int i = 0; i < fileIds.size(); i++) {
	        	SysFile file = fileService.findById(fileIds.get(i));
	        	file.setReferenceId(entity.getId());
	        	sysFiles.add(file);
	    	}
	    	fileService.saveAll(sysFiles);
	    }
		return entity;
	}

	@Override
	public List<MaterialExistRes> queryExistList(MaterialExistQuery query) {
		List<MaterialExistRes> list = null;
		if(query == null || StringUtils.isBlank(query.getKeywords())) {
			return list;
		}
		PredicateBuilder<BizMaterialAudit> builder = Specifications.and();
		builder.like("materialName", "%" + query.getKeywords() + "%");
		builder.eq("auditState", 0);
		builder.ne("operationStatus", 2);
		if(query.getExcludeIds() != null && !query.getExcludeIds().isEmpty()) {
			builder.notIn("id", query.getExcludeIds());
		}
		List<BizMaterialAudit> bmaList = this.baseRepository.findAll(builder.build());
		if(bmaList != null) {
			list = BeanUtils.listCopy(bmaList, MaterialExistRes.class);
		}
		return list;
	}
}