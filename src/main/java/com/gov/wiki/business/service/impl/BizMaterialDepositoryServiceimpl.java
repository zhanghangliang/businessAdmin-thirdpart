package com.gov.wiki.business.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizMaterialDepositoryDao;
import com.gov.wiki.business.dao.BizOpinionMaterialAuditDao;
import com.gov.wiki.business.dao.BizOpinionMaterialDao;
import com.gov.wiki.business.dao.BizSubjectMaterialAuditDao;
import com.gov.wiki.business.dao.BizSubjectMaterialDao;
import com.gov.wiki.business.req.MaterialQuery;
import com.gov.wiki.business.req.query.MaterialExistQuery;
import com.gov.wiki.business.res.MaterialExistRes;
import com.gov.wiki.business.service.BizMaterialDepositoryService;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.BeanUtils;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.ReqBean;

/**
 * @ClassName: BizMaterialDepositoryServiceimpl
 * @Description: 资料库管理业务处理接口实现
 * @author cys
 * @date 2020年11月24日
 */
@Service
public class BizMaterialDepositoryServiceimpl extends BaseServiceImpl<BizMaterialDepository,String, BizMaterialDepositoryDao> implements BizMaterialDepositoryService {
    
	@Autowired
	private BizSubjectMaterialAuditDao bizSubjectMaterialAuditDao;
	
	@Autowired
	private BizSubjectMaterialDao bizSubjectMaterialDao;
	
	@Autowired
	private BizOpinionMaterialAuditDao bizOpinionMaterialAuditDao;
	
	@Autowired
	private BizOpinionMaterialDao bizOpinionMaterialDao;
	
	@Override
    public Page<BizMaterialDepository> findAll(Specification specification, PageRequest pageRequest) {
        return this.baseRepository.findAll(specification,pageRequest);
    }

	@Override
	public void deleteById(String id) {
		if(id == null) return;
		super.deleteById(id);
	}
	
	@Override
	public Page<BizMaterialDepository> pageList(ReqBean<MaterialQuery> bean) {
		String userId = JwtUtil.getUserId();
		CheckUtil.notEmpty(userId, ResultCode.COMMON_ERROR, "用户未登录！");
		MaterialQuery query = bean.getBody() == null?new MaterialQuery():bean.getBody();
		PredicateBuilder<BizMaterialDepository> builder = Specifications.and();
		//builder.eq("createBy", userId);
		if(StringUtils.isNotBlank(query.getKeywords())) {
			builder.predicate(Specifications.or()
				.like("materialName", "%" + query.getKeywords() + "%")
				.like("version", "%" + query.getKeywords() + "%")
				.build());
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
		if(query.getRecyclingMark() != null) {
			builder.eq("recyclingMark", query.getRecyclingMark());
		}
		return this.baseRepository.findAll(builder.build(), bean.getHeader().getPageable());
	}

	@Override
	public List<MaterialExistRes> queryExistList(MaterialExistQuery query) {
		List<MaterialExistRes> list = null;
		if(query == null || StringUtils.isBlank(query.getKeywords())) {
			return list;
		}
		PredicateBuilder<BizMaterialDepository> builder = Specifications.and();
		builder.like("materialName", "%" + query.getKeywords() + "%");
		builder.eq("recyclingMark", false);
		if(query.getExcludeIds() != null && !query.getExcludeIds().isEmpty()) {
			builder.notIn("id", query.getExcludeIds());
		}
		List<BizMaterialDepository> bmdList = this.baseRepository.findAll(builder.build());
		if(bmdList != null) {
			list = BeanUtils.listCopy(bmdList, MaterialExistRes.class);
		}
		return list;
	}

	@Override
	public void recoveryMaterial(String materialId) {
		if(StringUtils.isBlank(materialId)) {
			return;
		}
		this.baseRepository.recoveryMaterial(materialId);
	}

	@Override
	public void replaceMaterialReference(String oldId, String newId) {
		if(StringUtils.isBlank(oldId) || StringUtils.isBlank(newId)) {
			return;
		}
		bizSubjectMaterialAuditDao.replaceMaterialReference(oldId, newId);
		bizSubjectMaterialDao.replaceMaterialReference(oldId, newId);
		bizOpinionMaterialAuditDao.replaceMaterialReference(oldId, newId);
		bizOpinionMaterialDao.replaceMaterialReference(oldId, newId);
	} 
}
