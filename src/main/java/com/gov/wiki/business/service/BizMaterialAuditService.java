package com.gov.wiki.business.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.gov.wiki.business.req.MaterialQuery;
import com.gov.wiki.business.req.MaterialReq;
import com.gov.wiki.business.req.query.MaterialExistQuery;
import com.gov.wiki.business.res.MaterialExistRes;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMaterialAudit;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.ReqBean;

/**
 * @ClassName: BizMaterialAuditService
 * @Description: 资料审核记录管理业务处理接口
 * @author cys
 * @date 2020年11月24日
 */
public interface BizMaterialAuditService extends IBaseService<BizMaterialAudit, String> {
    ResultBean<List<String>> findAllAuditMaterialType(List<String> creater);
    Page<BizMaterialAudit> findAll(Specification specification, PageRequest pageRequest);
    
	long findCountByMaterialId(String materialId);
	
	/**
	 * @Title: pageList
	 * @Description: 分页查询资料审核记录信息
	 * @param bean
	 * @return Page<BizMaterialAudit> 返回类型
	 * @throws
	 */
	Page<BizMaterialAudit> pageList(ReqBean<MaterialQuery> bean);
	
	/**
	 * @Title: saveOrUpdate
	 * @Description: 新增或者修改资料审核记录信息
	 * @param req
	 * @return BizMaterialAudit 返回类型
	 * @throws
	 */
	BizMaterialAudit saveOrUpdate(MaterialReq req);
	
	/**
	 * @Title: queryExistList
	 * @Description: 查询存在资料审核库信息
	 * @param query
	 * @return List<MaterialExistRes> 返回类型
	 * @throws
	 */
	List<MaterialExistRes> queryExistList(MaterialExistQuery query);
}
