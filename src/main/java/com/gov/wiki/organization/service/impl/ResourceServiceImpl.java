/**
 * @Title: ResourceServiceImpl.java 
 * @Package com.gov.wiki.organization.service.impl
 * @Description: 资源管理Service接口实现
 * @author cys 
 * @date 2019年11月7日 下午9:49:38 
 * @version V1.0 
 */
package com.gov.wiki.organization.service.impl;

import java.util.Date;
import java.util.List;
import javax.persistence.criteria.Predicate;

import com.github.wenhao.jpa.Sorts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.PrivResourceDao;
import com.gov.wiki.organization.req.query.ResourceQuery;
import com.gov.wiki.organization.service.IResourceService;
import com.gov.wiki.organization.service.IRoleResourceService;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<PrivResource, String, PrivResourceDao> implements IResourceService{
	
	/**
	 * 注入roleResourceService
	 */
	@Autowired
	private IRoleResourceService roleResourceService;

	@Override
	public void iteratResourceTree(PrivResource r, List<String> ids) {
		if(r == null) {
			return;
		}
		ids.add(r.getId());
		List<PrivResource> cList = r.getChildList();
		for(PrivResource re:cList) {
			iteratResourceTree(re, ids);
		}
	}

	@Override
	public void batchDelByIds(List<String> ids) {
		this.baseRepository.batchDelByIds(new Date(), ids);
		roleResourceService.delRRByResourceIds(ids);
	}

	@Override
	public List<PrivResource> findByParams(ResourceQuery rq) {
		rq = rq == null?new ResourceQuery():rq;
		if(StringUtils.isBlank(rq.getKeywords()) && StringUtils.isBlank(rq.getParentId())) {
			rq.setParentId("-1");
		}
		final ResourceQuery param = rq;
		Specification<PrivResource> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getParentId())) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("parentId").as(String.class), param.getParentId()));
			}
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("componentName").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("perms").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("name").as(String.class), "%" + param.getKeywords() + "%")
				));
			}

			return predicate;
		};
		Sort sort= Sorts.builder().desc("sortNo").build();
		List<PrivResource> resourceList = this.baseRepository.findAll(spec,sort);
		return resourceList;
	}

	@Override
	public List<PrivResource> findByRoleId(List<String> ids) {
		return baseRepository.findByRoleIds(ids);
	}

	@Override
	public List<Object[]> groupParentId() {
		return this.baseRepository.groupParentId();
	}

	@Override
	public List<Object[]> findIdByParentId(String parentid) {
		return this.baseRepository.findIdByParentId(parentid);
	}

	@Override
	public List<PrivResource> findByParentId(String parentid,List<String> ids) {
		return this.baseRepository.findByParentIdAndIdIn(parentid,ids);
	}

	@Override
	public List<PrivResource> findByParentIdAndIdIn(String parentid, List<String> ids) {
		return this.baseRepository.findByParentIdAndIdIn(parentid,ids);
	}

	@Override
	public List<Object[]> groupParentId2(List<String> ids) {
		return this.baseRepository.groupParentId2(ids);
	}

	@Override
	public List<Object[]> findIdByParentId2(String parentid,List<String> ids) {
		return this.baseRepository.findIdByParentId2(parentid,ids);
	}
}