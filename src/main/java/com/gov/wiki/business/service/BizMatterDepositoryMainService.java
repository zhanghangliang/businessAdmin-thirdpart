package com.gov.wiki.business.service;

import com.gov.wiki.business.req.MatterQuery;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.ReqBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public interface BizMatterDepositoryMainService extends IBaseService<BizMatterDepositoryMain, String> {
	List<BizMatterDepositoryMain> findByUpmatterId(String upmatterid);

	Page<BizMatterDepositoryMain> findAll(Specification specification, PageRequest pageRequest);

	List<Object[]> groupUpMatterId(List<String> creater);

	List<Object[]> findIdByUpmatterId(String upmatterid, List<String> creater);

	List<BizMatterDepositoryMain> findsonbyids(List<String> ids);

	Page<BizMatterDepositoryMain> findAll(Specification specification, Pageable pageable);

	List<BizMatterDepositoryMain> findAll(Specification specification);

	List<String> findOnline();
	
	/**
	 * @Title: pageDepositoryMetter
	 * @Description: 分页查询事项库数据
	 * @param bean
	 * @return ResultBean<PageInfo> 返回类型
	 * @throws
	 */
	ResultBean<PageInfo> pageDepositoryMetter(ReqBean<MatterQuery> bean);
	
	/**
	 * @Title: queryChildsByParent
	 * @Description: 根据父ID查询子事项
	 * @param parentId
	 * @return List<BizMatterDepositoryMain> 返回类型
	 * @throws
	 */
	List<BizMatterDepositoryMain> queryChildsByParent(String parentId);
}