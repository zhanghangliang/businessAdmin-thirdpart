package com.gov.wiki.business.service;

import com.gov.wiki.business.req.MaterialQuery;
import com.gov.wiki.business.req.query.MaterialExistQuery;
import com.gov.wiki.business.res.MaterialExistRes;
import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public interface BizMaterialDepositoryService extends IBaseService<BizMaterialDepository, String> {
    Page<BizMaterialDepository> findAll(Specification specification, PageRequest pageRequest);
    
    /**
	 * @Title: pageList
	 * @Description: 分页查询资料库信息
	 * @param bean
	 * @return Page<BizMaterialDepository> 返回类型
	 * @throws
	 */
	Page<BizMaterialDepository> pageList(ReqBean<MaterialQuery> bean);
	
	/**
	 * @Title: queryExistList
	 * @Description: 查询存在资料库信息
	 * @param query
	 * @return List<MaterialExistRes> 返回类型
	 * @throws
	 */
	List<MaterialExistRes> queryExistList(MaterialExistQuery query);
	
	/**
	 * @Title: recoveryMaterial
	 * @Description: 回收资料信息
	 * @param materialId
	 * @return void 返回类型
	 * @throws
	 */
	void recoveryMaterial(String materialId);
	
	/**
	 * @Title: replaceMaterialReference
	 * @Description: 替换资料关联信息
	 * @param oldId
	 * @param newId
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	void replaceMaterialReference(String oldId, String newId);
}
