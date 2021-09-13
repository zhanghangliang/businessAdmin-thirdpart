/**
 * @Title: RoleResourceServiceImpl.java 
 * @Package com.insolu.spm.organization.service.impl 
 * @Description: 角色资源关系管理Service接口实现
 * @author cys 
 * @date 2019年11月7日 下午9:52:20 
 * @version V1.0 
 */
package com.gov.wiki.organization.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.entity.system.PrivRoleResource;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.PrivRoleResourceDao;
import com.gov.wiki.organization.service.IResourceService;
import com.gov.wiki.organization.service.IRoleResourceService;
import com.gov.wiki.organization.service.IRoleService;

@Service("roleResourceService")
public class RoleResourceServiceImpl extends BaseServiceImpl<PrivRoleResource, String, PrivRoleResourceDao> implements IRoleResourceService{

	/**
	 * 注入roleService
	 */
	@Autowired
	private IRoleService roleService;
	
	/**
	 * 注入resourceService
	 */
	@Autowired
	private IResourceService resourceService;
	
	@Override
	public void delRRByResourceIds(List<String> resourceIds) {
		this.baseRepository.delRRByResourceIds(resourceIds);
	}

	@Override
	public void delRRByRoleIds(List<String> roleIds) {
		this.baseRepository.delRRByRoleIds(roleIds);
	}

	@Override
	@Transactional
	public void updateRoleResource(String roleId, List<String> resourceIds) {
		if(StringUtils.isBlank(roleId)) {
			return;
		}
		OrgRole role = this.roleService.findById(roleId);
		if(role == null) {
			return;
		}
		this.baseRepository.deleteByRoleId(roleId);
		if(resourceIds == null || resourceIds.isEmpty()) {
			return;
		}
		List<PrivResource> rList = resourceService.findByIds(resourceIds);
		List<PrivRoleResource> rrList = new ArrayList<PrivRoleResource>();
		if(rList != null && !rList.isEmpty()) {
			for(PrivResource re :rList) {
				PrivRoleResource rr = new PrivRoleResource();
				rr.setResourceId(re.getId());
				rr.setRoleId(role.getId());
				rrList.add(rr);
			}
		}
		if(!rrList.isEmpty()) {
			this.saveAll(rrList);
		}
	}

	@Override
	public List<PrivRoleResource> findByRoleId(String roleId) {
		if(StringUtils.isBlank(roleId)) {
			return null;
		}
		return this.baseRepository.findByRoleId(roleId);
	}

	@Override
	public List<PrivRoleResource> findByResourceId(String resourceId) {
		return this.baseRepository.findByResourceId(resourceId);
	}

	@Override
	public List<PrivRoleResource> findByResourceIds(List<String> ids) {
		return this.baseRepository.findByResourceIds(ids);
	}

	@Override
	public List<String> findByRoleIds(List<String> roleIds) {
		return this.baseRepository.findByRoleIds(roleIds);
	}
}