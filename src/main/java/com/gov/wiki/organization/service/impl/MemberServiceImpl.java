/**
 * @Title: MemberServiceImpl.java
 * @Package com.gov.wiki.organization.service.impl
 * @Description: 人员管理处理接口实现
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service.impl;

import java.util.*;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.alibaba.fastjson.JSON;
import com.gov.wiki.common.entity.MemberInfoReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.JwtUtil;
import com.gov.wiki.common.utils.PageInfo;
import com.gov.wiki.common.utils.PageableTools;
import com.gov.wiki.common.utils.PasswordUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.SortTools;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.OrgMemberDao;
import com.gov.wiki.organization.req.ChangePwd;
import com.gov.wiki.organization.req.LoginReq;
import com.gov.wiki.organization.req.query.MemberQuery;
import com.gov.wiki.organization.res.LoginRes;
import com.gov.wiki.organization.service.IDepartService;
import com.gov.wiki.organization.service.IJobService;
import com.gov.wiki.organization.service.IMemberRoleService;
import com.gov.wiki.organization.service.IMemberService;
import com.gov.wiki.organization.service.IPostService;
import com.gov.wiki.organization.service.IResourceService;
import com.gov.wiki.organization.service.IRoleService;
import com.gov.wiki.system.dao.SysLogDao;

@Service("memberService")
public class MemberServiceImpl extends BaseServiceImpl<OrgMember, String, OrgMemberDao> implements IMemberService {
	/**
	 * 注入departService
	 */
	@Autowired
	private IDepartService departService;
	/**
	 * 注入postService
	 */
	@Autowired
	private IPostService postService;
	/**
	 * 注入jobService
	 */
	@Autowired
	private IJobService jobService;
	/**
	 * 注入memberRoleService
	 */
	@Autowired
	private IMemberRoleService memberRoleService;
	@Autowired
	private SysLogDao logDao;
	@Autowired
	private RedisManager redisManager;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IResourceService resourceService;
	@Autowired
	private OrgMemberDao orgMemberDao;

	
	@Override
	public LoginRes userLogin(LoginReq req) {
		LoginRes res = new LoginRes();
		CheckUtil.notNull(req, ResultCode.COMMON_ERROR, "请求参数不存在");
		CheckUtil.notEmpty(req.getUsername(), ResultCode.COMMON_ERROR, "登录用户名不能为空");
		CheckUtil.notEmpty(req.getPassword(), ResultCode.COMMON_ERROR, "登录密码不能为空");
		OrgMember member = this.baseRepository.getByUsername(req.getUsername());
		CheckUtil.notNull(member, ResultCode.COMMON_ERROR, "用户不存在");
		//检测是否停用
		CheckUtil.check(checkStatus(member), ResultCode.COMMON_ERROR, "用户被禁用");
		//校验密码
		CheckUtil.check(PasswordUtil.checkEncryption(req.getPassword(), member.getPassword()), ResultCode.COMMON_ERROR, "用户名或密码错误");
		//查询角色
		List<OrgRole> roles = roleService.findByUserId(member.getId());
		CheckUtil.check(roles.size()!=0, ResultCode.COMMON_ERROR, "未分配角色");
		List<PrivResource> resources = getResources(roles);
		List<PrivResource> treeResource = new ArrayList<PrivResource>();
		List<String> resourceIds = new ArrayList<String>();
		List<String> hasResourceIds = new ArrayList<String>();
		if(resources != null && !resources.isEmpty()) {
			for(PrivResource pr:resources) {
				resourceIds.add(pr.getId());
				if(StringUtils.isBlank(pr.getParentId()) || "-1".equals(pr.getParentId())) {
					if(!hasResourceIds.contains(pr.getId())) {
						treeResource.add(pr);
						hasResourceIds.add(pr.getId());
					}
				}
			}
		}
		removeNonPermissionResources(treeResource, resourceIds);
		treeResource.sort(Comparator.comparing(PrivResource :: getSortNo));
		res.setMember(member).setRoles(roles).setTreeResources(treeResource);
		return res;
	}
	
	private List<PrivResource> getResources(List<OrgRole> roles) {
		if(roles.isEmpty()) return new ArrayList<PrivResource>();
		List<String> ids = new ArrayList<String>(roles.size());
		for (OrgRole role : roles) {
			ids.add(role.getId());
		}
		//查询资源列表
		List<PrivResource> resources = resourceService.findByRoleId(ids);
		return resources;
	}
	
	/**
	 * 校验状态
	 * @param member
	 * @return
	 */
	private boolean checkStatus(OrgMember member) {
		if(member.getStatus() == null) {
			return false;
		}
		return member.getStatus() == StatusEnum.ENABLE.getValue();
	}
	
	private void removeNonPermissionResources(List<PrivResource> treeResource, List<String> resourceIds) {
		if(treeResource == null || treeResource.isEmpty()) {
			return;
		}
		for(int i=0;i < treeResource.size(); i++) {
			PrivResource pr = treeResource.get(i);
			if(!resourceIds.contains(pr.getId())) {
				treeResource.remove(pr);
				i--;
			}else {
				if(pr.getChildList() != null) {
					removeNonPermissionResources(pr.getChildList(), resourceIds);
				}
			}
		}
	}

	@Override
	public ResultBean<String> saveOrUpdateMember(OrgMember member) {
		CheckUtil.notNull(member, ResultCode.COMMON_ERROR, "请求参数为空!");
		CheckUtil.notEmpty(member.getUsername(), ResultCode.COMMON_ERROR, "登录名不能为空!");
		CheckUtil.notEmpty(member.getRealName(), ResultCode.COMMON_ERROR, "姓名不能为空!");
		orgMemberDao.save(member);
//		OrgMember old = null;
//		if(member.getId() != null) {
//			old = this.findById(member.getId());
//		}
//		if(old == null) {
//			CheckUtil.notEmpty(member.getPassword(), ResultCode.COMMON_ERROR, "密码不能为空!");
//			if(member.getAccountType() == null) {
//				member.setAccountType(2);
//			}
//		}else {
//			member.setAccountType(old.getAccountType());
//		}
//		OrgDepart depart = null;
//		if(member.getDepart() != null && member.getDepart().getId() != null) {
//			depart = departService.findById(member.getDepart().getId());
//		}
//		CheckUtil.notNull(depart, ResultCode.COMMON_ERROR, "所属部门不能为空!");
//		OrgPost post = null;
//		if(member.getMainPost() != null && member.getMainPost().getId() != null) {
//			post = postService.findById(member.getMainPost().getId());
//		}
//		CheckUtil.notNull(post, ResultCode.COMMON_ERROR, "主岗不能为空!");
//		OrgJob job = null;
//		if(member.getJob() != null && member.getJob().getId() != null) {
//			job = jobService.findById(member.getJob().getId());
//		}
//		CheckUtil.notNull(job, ResultCode.COMMON_ERROR, "职务级别不能为空!");
//		CheckUtil.check(!existSameUsername(member.getUsername(), old), ResultCode.COMMON_ERROR, "存在相同的用户名!");
//		if(old == null) {
//			String encodePwd = PasswordUtil.encryption(member.getPassword());
//			member.setPassword(encodePwd);
//		}else {
//			member.setId(old.getId());
//			if(StringUtils.isNotBlank(member.getPassword())) {
//				String encodePwd = PasswordUtil.encryption(member.getPassword());
//				member.setPassword(encodePwd);
//			}else {
//				member.setPassword(old.getPassword());
//			}
//		}
//		this.saveOrUpdate(member);
//		List<String> roleIds = new ArrayList<String>();
//		List<OrgRole> roleList = member.getRoleList();
//		if(roleList != null && !roleList.isEmpty()) {
//			for(OrgRole r:roleList) {
//				if(StringUtils.isBlank(r.getId())) {
//					continue;
//				}
//				roleIds.add(r.getId());
//			}
//		}
//		memberRoleService.updateMemberRole(member.getId(), roleIds);
		return new ResultBean();
	}

	@Override
	public boolean existSameUsername(String username, OrgMember old) {
		boolean flag = false;
		List<OrgMember> mList = this.baseRepository.findByUsername(username);
		if(mList != null && !mList.isEmpty()) {
			for(OrgMember m:mList) {
				if(old == null || (old != null && !m.getId().equals(old.getId()))) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public ResultBean delMemberById(String memberId) {
		OrgMember member = findById(memberId);
		if(member != null) {
			member.setDelFlag(true);
			saveOrUpdate(member);
		}
		memberRoleService.delByMemberId(memberId);
		return new ResultBean();
	}

	@Override
	public ResultBean batchDelMemberByIds(List<String> ids) {
		List<OrgMember> mList = this.findByIds(ids);
		if(mList != null && !mList.isEmpty()) {
			for(OrgMember m:mList) {
				m.setDelFlag(true);
			}
			this.saveAll(mList);
		}
		memberRoleService.batchDelByMemberIds(ids);
		return new ResultBean();
	}

	@Override
	public ResultBean<PageInfo> pageMemberList(ReqBean<MemberQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		final MemberQuery param = bean.getBody() == null?new MemberQuery():bean.getBody();
		Specification<OrgMember> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("code").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("mobile").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("username").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("realName").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			if(StringUtils.isNotBlank(param.getDepartId())) {
				Join<OrgMember, OrgDepart> joinDepart = root.join("depart", JoinType.LEFT);
				if(param.isAllPathMember()) {
					String path = departService.queryDepartPath(param.getDepartId());
					predicate.getExpressions().add(criteriaBuilder.like(joinDepart.get("path").as(String.class), path + "%"));
				}else {
					predicate.getExpressions().add(criteriaBuilder.equal(joinDepart.get("id").as(String.class), param.getDepartId()));
				}
			}
			if(StringUtils.isNotBlank(param.getPostId())) {
				Join<OrgMember, OrgPost> joinPost = root.join("mainPost", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinPost.get("id").as(String.class), param.getPostId()));
			}
			if(StringUtils.isNotBlank(param.getJobId())) {
				Join<OrgMember, OrgJob> joinJob = root.join("job", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinJob.get("id").as(String.class), param.getJobId()));
			}
			if(StringUtils.isNotBlank(param.getMemberState())) {
				Join<OrgMember, SysDictItem> joinMemberState = root.join("memberState", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinMemberState.get("id").as(String.class), param.getMemberState()));
			}
			if(StringUtils.isNotBlank(param.getMemberType())) {
				Join<OrgMember, SysDictItem> joinMemberType = root.join("memberType", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinMemberType.get("id").as(String.class), param.getMemberType()));
			}
			if(param.getSex() != null) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("sex").as(String.class), param.getSex()));
			}
			if(param.getStatus() != null) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("status").as(String.class), param.getStatus()));
			}
            return predicate;
        };
 
		Page<OrgMember> page = this.baseRepository.findAll(spec, pageable);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public int countMember() {
		return this.baseRepository.countMember();
	}

	@Override
	public ResultBean<List<OrgMember>> queryMemberList(ReqBean<MemberQuery> bean) {
		PageInfo pageInfo = bean.getPage();
		Sort sort = SortTools.basicSort(pageInfo.getSortList());
		final MemberQuery param = bean.getBody() == null?new MemberQuery():bean.getBody();
		Specification<OrgMember> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("code").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("mobile").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("username").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("realName").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			if(StringUtils.isNotBlank(param.getDepartId())) {
				Join<OrgMember, OrgDepart> joinDepart = root.join("depart", JoinType.LEFT);
				if(param.isAllPathMember()) {
					String path = departService.queryDepartPath(param.getDepartId());
					predicate.getExpressions().add(criteriaBuilder.like(joinDepart.get("path").as(String.class), path + "%"));
				}else {
					predicate.getExpressions().add(criteriaBuilder.equal(joinDepart.get("id").as(String.class), param.getDepartId()));
				}
			}
			if(StringUtils.isNotBlank(param.getPostId())) {
				Join<OrgMember, OrgPost> joinPost = root.join("mainPost", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinPost.get("id").as(String.class), param.getPostId()));
			}
			if(StringUtils.isNotBlank(param.getJobId())) {
				Join<OrgMember, OrgJob> joinJob = root.join("job", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinJob.get("id").as(String.class), param.getJobId()));
			}
			if(StringUtils.isNotBlank(param.getMemberState())) {
				Join<OrgMember, SysDictItem> joinMemberState = root.join("memberState", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinMemberState.get("id").as(String.class), param.getMemberState()));
			}
			if(StringUtils.isNotBlank(param.getMemberType())) {
				Join<OrgMember, SysDictItem> joinMemberType = root.join("memberType", JoinType.LEFT);
				predicate.getExpressions().add(criteriaBuilder.equal(joinMemberType.get("id").as(String.class), param.getMemberType()));
			}
			if(param.getSex() != null) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("sex").as(String.class), param.getSex()));
			}
			if(param.getStatus() != null) {
				predicate.getExpressions().add(criteriaBuilder.equal(root.get("status").as(String.class), param.getStatus()));
			}
			if(param.getDepartRanges() != null && !param.getDepartRanges().isEmpty()) {// 查询分管部门下所有人员
				Predicate disjunctPredicate = criteriaBuilder.disjunction();
				Join<OrgMember, OrgDepart> joinDepart = root.join("depart", JoinType.LEFT);
				for(String path:param.getDepartRanges()) {
					disjunctPredicate.getExpressions().add(criteriaBuilder.like(joinDepart.get("path").as(String.class), path + "%"));
				}
				String userId = JwtUtil.getUserId();
				if(StringUtils.isNotBlank(userId)) {
					disjunctPredicate.getExpressions().add(criteriaBuilder.equal(root.get("id").as(String.class), userId));
				}
				predicate.getExpressions().add(disjunctPredicate);
			}
            return predicate;
        };
        List<OrgMember> mList = this.baseRepository.findAll(spec, sort);
		return new ResultBean<List<OrgMember>>(mList);
	}

	@Override
	public ResultBean<PageInfo> pageMembersByRoleId(ReqBean<String> bean,List<String> creater) {
		PageInfo pageInfo = bean.getPage();
		Pageable pageable = PageableTools.basicPage(pageInfo.getCurrentPage(), pageInfo.getPageSize(), pageInfo.getSortList());
		Page<OrgMember> page = this.baseRepository.findMemberByRoleId(bean.getBody(), pageable,creater);
		pageInfo.setDataList(page.getContent());
		pageInfo.setTotalPages(page.getTotalPages());
		pageInfo.setTotal(page.getTotalElements());
		return new ResultBean(pageInfo);
	}

	@Override
	public List<OrgMember> queryMemberByDepartPath(String path) {
		if(StringUtils.isBlank(path)) {
			return null;
		}
		Specification<OrgMember> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            Join<OrgMember, OrgDepart> joinDepart = root.join("depart", JoinType.LEFT);
            predicate.getExpressions().add(criteriaBuilder.like(joinDepart.get("path").as(String.class), path + "%"));
            return predicate;
		};
		List<OrgMember> mList = this.baseRepository.findAll(spec);
		return mList;
	}

	@Override
	public List<OrgMember> queryMemberByDepartPaths(List<String> paths, String userId) {
		if((paths == null || paths.isEmpty()) && StringUtils.isBlank(userId)) {
			return null;
		}
		Specification<OrgMember> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.disjunction();
            Join<OrgMember, OrgDepart> joinDepart = root.join("depart", JoinType.LEFT);
            if(paths != null && !paths.isEmpty()) {
            	for(String path:paths) {
                	predicate.getExpressions().add(criteriaBuilder.like(joinDepart.get("path").as(String.class), path + "%"));
                }
            }
            if(StringUtils.isNotBlank(userId)) {
            	predicate.getExpressions().add(criteriaBuilder.equal(root.get("id").as(String.class), userId));
            }
            return predicate;
		};
		List<OrgMember> mList = this.baseRepository.findAll(spec);
		return mList;
	}

	@Override
	public void changePwd(ChangePwd body) {
		CheckUtil.notNull(body, ResultCode.COMMON_ERROR, "请求参数不能为空");
		CheckUtil.notEmpty(body.getOldPwd(), ResultCode.PARAM_NULL, "旧密码");
		CheckUtil.notEmpty(body.getNewPwd(), ResultCode.PARAM_NULL, "新密码");
		String userId = JwtUtil.getUserId();
		CheckUtil.notEmpty(userId, ResultCode.COMMON_ERROR, "未登录");
		Optional<OrgMember> o = baseRepository.findById(userId);
		CheckUtil.check(o.isPresent(), ResultCode.COMMON_ERROR, "当前登录信息不存在");
		OrgMember member = o.get();
		CheckUtil.check(PasswordUtil.getSaltverifyMD5(body.getOldPwd(), member.getPassword()), ResultCode.COMMON_ERROR, "密码错误");
		String newPwd = PasswordUtil.getSaltMD5(body.getNewPwd());
		member.setPassword(newPwd);
		baseRepository.save(member);
	}

	@Override
	public List<MemberInfoReq> findNameByIds(List<String> ids) {
		return baseRepository.findNameByIds(ids);
	}

	@Override
	public OrgMember getByUsername(String username) {
		return this.baseRepository.getByUsername(username);
	}

	@Override
	public List<MemberInfoReq> findByDepartId(String id) {
		return orgMemberDao.findByDepartId(id);
	}

	@Override
	public List<MemberInfoReq> getAllmember(String companyId) {
		return orgMemberDao.getAllmember(companyId);
	}

	@Override
	public Page<MemberInfoReq> findAll(Specification specification, PageRequest pageRequest) {
		return this.baseRepository.findAll(specification,pageRequest);
	}

	@Override
	public List<String> findIdByCompanyId(String companyid) {
		return this.baseRepository.findId(companyid);
	}

	@Override
	public String findNamesByIds(List<String> ids) {
		String names = "";
		if(ids == null || ids.isEmpty()) {
			return names;
		}
		List<OrgMember> mList = this.findByIds(ids);
		List<String> nameList = new ArrayList<String>();
		if(mList != null && !mList.isEmpty()) {
			for(OrgMember m:mList) {
				if(m == null || StringUtils.isBlank(m.getRealName())) {
					continue;
				}
				nameList.add(m.getRealName());
			}
		}
		if(!nameList.isEmpty()) {
			names = String.join(",", nameList);
		}
		return names;
	}
}