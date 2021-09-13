/**
 * @Title: MemberRoleServiceImpl.java
 * @Package com.spm.system.service.impl
 * @Description: 人员角色管理处理接口实现
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivMemberRole;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.PrivMemberRoleDao;
import com.gov.wiki.organization.service.IMemberRoleService;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.organization.service.IRoleService;

import javax.transaction.Transactional;

@Service("memberRoleService")
public class MemberRoleServiceImpl extends BaseServiceImpl<PrivMemberRole, String, PrivMemberRoleDao> implements IMemberRoleService {

	/**
	 * 注入roleService
	 */
	@Autowired
	private IRoleService roleService;
	/**
	 * 注入memberService
	 */
	@Autowired
	private IMemberService memberService;
	
	@Override
	public void delByRoleId(String roleId) {
		if(StringUtils.isBlank(roleId)) {
			return;
		}
		this.baseRepository.deleteByRoleId(roleId);
	}

	@Override
	public void batchDelByRoleIds(List<String> roleIds) {
		if(roleIds == null || roleIds.isEmpty()) {
			return;
		}
		this.baseRepository.batchDelByRoleIds(roleIds);
	}

	@Override
	public void updateMemberRole(String memberId, List<String> roleIds) {
		if(StringUtils.isBlank(memberId)) {
			return;
		}
		this.baseRepository.deleteByMemberId(memberId);
		List<PrivMemberRole> mrList = new ArrayList<PrivMemberRole>();
		if(roleIds != null && !roleIds.isEmpty()) {
			List<OrgRole> roleList = roleService.findByIds(roleIds);
			if(roleList != null && !roleList.isEmpty()) {
				for(OrgRole role:roleList) {
					PrivMemberRole mr = new PrivMemberRole();
					mr.setMemberId(memberId);
					mr.setRoleId(role.getId());
					mrList.add(mr);
				}
			}
		}
		if(!mrList.isEmpty()) {
			this.saveAll(mrList);
		}
	}

	@Override
	public void delByMemberId(String memberId) {
		if(StringUtils.isBlank(memberId)) {
			return;
		}
		this.baseRepository.deleteByMemberId(memberId);
	}

	@Override
	public void batchDelByMemberIds(List<String> memberIds) {
		if(memberIds == null || memberIds.isEmpty()) {
			return;
		}
		this.baseRepository.batchDelByMemberIds(memberIds);
	}

	@Override
	public void updateRoleMembers(String roleId, List<String> memberIds) {
		if(StringUtils.isBlank(roleId)) {
			return;
		}
		OrgRole role = this.roleService.findById(roleId);
		if(role == null) {
			return;
		}
		this.baseRepository.deleteByRoleId(roleId);
		if(memberIds == null || memberIds.isEmpty()) {
			return;
		}
		List<OrgMember> mList = memberService.findByIds(memberIds);
		List<PrivMemberRole> mrList = new ArrayList<PrivMemberRole>();
		if(mList != null && !mList.isEmpty()) {
			for(OrgMember m :mList) {
				PrivMemberRole mr = new PrivMemberRole();
				mr.setMemberId(m.getId());
				mr.setRoleId(role.getId());
				mrList.add(mr);
			}
		}
		if(!mrList.isEmpty()) {
			this.saveAll(mrList);
		}
	}

	@Override
	public List<PrivMemberRole> findByRoleId(String roleId) {
		if(StringUtils.isBlank(roleId)) {
			return null;
		}
		return this.baseRepository.findByRoleId(roleId);
	}

	@Override
	public List<PrivMemberRole> findByMemberId(List<String> id) {
		return this.baseRepository.findByMemberIdIn(id);
	}

	@Override
	public List<PrivMemberRole> findByRoleIds(List<String> ids) {
		return this.baseRepository.findByRoleIds(ids);
	}
}