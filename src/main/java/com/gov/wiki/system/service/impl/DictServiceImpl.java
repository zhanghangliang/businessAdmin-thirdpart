/**
 * @Title: DictServiceImpl.java 
 * @Package com.gov.wiki.system.service.impl
 * @Description: 数据字典管理Service接口实现
 * @author cys 
 * @date 2019年11月5日 下午9:26:54 
 * @version V1.0 
 */
package com.gov.wiki.system.service.impl;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.entity.system.SysDict;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SortTools;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.dao.SysDictDao;
import com.gov.wiki.system.req.query.DictQuery;
import com.gov.wiki.system.service.IDictService;

/**
 * @ClassName: DictServiceImpl
 * @Description: 字典管理
 * @author cys
 * @date 2020年5月18日
 */
@Service
public class DictServiceImpl extends BaseServiceImpl<SysDict, String, SysDictDao> implements IDictService{

	@Override
	public SysDict getByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}
		List<SysDict> dictList = baseRepository.findByDictCode(code);
		return dictList != null && !dictList.isEmpty()?dictList.get(0):null;
	}

	@Override
	public boolean existDictCode(SysDict dict) {
		boolean flag = false;
		List<SysDict> dictList = baseRepository.findByDictCode(dict.getDictCode());
		if(dictList != null && !dictList.isEmpty()) {
			for(SysDict d:dictList) {
				if(!d.getId().equals(dict.getId())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public ResultBean<List<SysDict>> queryDictList(ReqBean<DictQuery> bean) {
		final DictQuery param = bean.getBody() == null?new DictQuery():bean.getBody();
		PageInfo pageInfo = bean.getPage();
		Sort sort = SortTools.basicSort(pageInfo.getSortList());
		Specification<SysDict> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("dictName").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("dictCode").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			return predicate;
		};
		List<SysDict> dictList = this.baseRepository.findAll(spec, sort);
		return new ResultBean<List<SysDict>>(dictList);
	}

	@Override
	public ResultBean<PageInfo> pageDictList(ReqBean<DictQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final DictQuery param = bean.getBody() == null?new DictQuery():bean.getBody();
		Specification<SysDict> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("dictName").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("dictCode").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			return predicate;
		};
		Page<SysDict> page = this.baseRepository.findAll(spec, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}
}
