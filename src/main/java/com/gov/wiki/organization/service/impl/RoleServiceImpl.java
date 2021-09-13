/**
 * @Title: RoleServiceImpl.java
 * @Package com.gov.wiki.organization.service.impl
 * @Description: 角色管理处理接口实现
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import com.alibaba.fastjson.JSON;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.entity.system.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SortTools;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.OrgRoleDao;
import com.gov.wiki.organization.req.DistribMemberReq;
import com.gov.wiki.organization.req.DistribResourceReq;
import com.gov.wiki.organization.req.query.RoleQuery;
import com.gov.wiki.organization.service.IMemberRoleService;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.organization.service.IResourceService;
import com.gov.wiki.organization.service.IRoleResourceService;
import com.gov.wiki.organization.service.IRoleService;

@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<OrgRole, String, OrgRoleDao> implements IRoleService {

	/**
	 * 注入memberRoleService
	 */
	@Autowired
	private IMemberRoleService memberRoleService;
	
	/**
	 * 注入roleResourceService
	 */
	@Autowired
	private IRoleResourceService roleResourceService;
	
	/**
	 * 注入memberService
	 */
	@Autowired
	private IMemberService memberService;
	
	/**
	 * 注入resourceService
	 */
	@Autowired
	private IResourceService resourceService;

	@Autowired
	private OrgRoleDao orgRoleDao;

	@Override
	public void delRoleById(String id) {
		CheckUtil.notEmpty(id, ResultCode.PARAM_NULL, "ID");
		OrgRole role = findById(id);
		if(role != null) {
			role.setDelFlag(true);
			saveOrUpdate(role);
			memberRoleService.delByRoleId(role.getId());
			List<String> roleIds = new ArrayList<String>();
			roleIds.add(role.getId());
			roleResourceService.delRRByRoleIds(roleIds);
		}
	}

	@Override
	public void batchDelRoleByIds(List<String> ids) {
		List<OrgRole> rList = findByIds(ids);
		List<String> roleIds = new ArrayList<String>();
		if(rList != null && !rList.isEmpty()) {
			for(OrgRole role:rList) {
				role.setDelFlag(true);
				roleIds.add(role.getId());
			}
			saveAll(rList);
			memberRoleService.batchDelByRoleIds(roleIds);
			roleResourceService.delRRByRoleIds(roleIds);
		}
	}
	
	@Override
	public List<OrgRole> findByUserId(String userId) {
		if(userId == null) return null;
		return baseRepository.findByUserId(userId);
	}

	@Override
	public ResultBean distribMember(DistribMemberReq req) {
		memberRoleService.updateRoleMembers(req.getRoleId(), req.getMemberIds());
		return new ResultBean();
	}

	@Override
	public ResultBean distribResource(DistribResourceReq req) {
		roleResourceService.updateRoleResource(req.getRoleId(), req.getResourceIds());
		return new ResultBean();
	}

	@Override
	public ResultBean<List<OrgMember>> queryMembersByRoleId(String roleId) {
		List<PrivMemberRole> mrList = this.memberRoleService.findByRoleId(roleId);
		List<String> mIds = new ArrayList<String>();
		if(mrList != null && !mrList.isEmpty()) {
			for(PrivMemberRole mr:mrList) {
				mIds.add(mr.getMemberId());
			}
		}
		List<OrgMember> mList = this.memberService.findByIds(mIds);
		return new ResultBean(mList);
	}

	@Override
	public ResultBean<HashMap<String,Object>> queryResourcesByRoleId(String roleId) {
		HashMap<String,Object> ans=new HashMap<>();
		List<PrivRoleResource> rrList = this.roleResourceService.findByRoleId(roleId);
		//存分配了那些资源
		List<String> rIds = new ArrayList<String>();
		if(rrList != null && !rrList.isEmpty()) {
			for(PrivRoleResource rr:rrList) {
				rIds.add(rr.getResourceId());
			}
			List<Object[]> topid = orgRoleDao.findtop(roleId);
			ans.put("0",topid);
			List<Object[]> objects = resourceService.groupParentId2(rIds);
			System.out.println(JSON.toJSONString(objects));
			for (Object[] object : objects) {
				List<Object[]> idByParentId = resourceService.findIdByParentId2(object[0].toString(),rIds);
				ans.put(object[0].toString(),idByParentId);
			}
		}
		return new ResultBean(ans);
	}

	@Override
	public ResultBean<PageInfo> pageRoleList(ReqBean<RoleQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final RoleQuery param = bean.getBody() == null?new RoleQuery():bean.getBody();
		Specification<OrgRole> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("name").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("code").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			if(param.getDefaultVal() != null) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("_default").as(String.class), param.getDefaultVal()));
			}
			return predicate;
		};
		Page<OrgRole> page = this.baseRepository.findAll(spec, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public ResultBean<List<OrgRole>> queryRoleList(ReqBean<RoleQuery> bean,List<String> creater) {
		final RoleQuery param = bean.getBody() == null?new RoleQuery():bean.getBody();

		Specification<OrgRole> specification1 = Specifications.<OrgRole>or()
				.like(param.getKeywords()!=null,"name","%"+param.getKeywords()+"%")
				.like(param.getKeywords()!=null,"code","%"+param.getKeywords()+"%")
				.build();
		Specification<OrgRole> specification = Specifications.<OrgRole>and()
				.eq(param.getDefaultVal()!=null,"defaultVal",param.getDefaultVal())
				.in("createBy",creater)
				.build()
				.and(specification1);
		List<OrgRole> roleList = this.baseRepository.findAll(specification);
		return new ResultBean(roleList);
	}

	@Override
	public Page<OrgRole> findAll(Specification specification, PageRequest pageRequest) {
		return this.baseRepository.findAll(specification,pageRequest);
	}
}