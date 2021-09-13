/**
 * @Title: DepartServiceImpl.java
 * @Package com.gov.wiki.organization.service.impl
 * @Description: 部门管理处理接口实现
 * @author cys
 * @date 2019年11月1日
 * @version V1.0
 */
package com.gov.wiki.organization.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.gov.wiki.common.beans.ResultBean;
import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.check.CheckClass;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.redis.RedisManager;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.LevelUtil;
import com.gov.wiki.common.utils.ReqBean;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.organization.dao.OrgDepartDao;
import com.gov.wiki.organization.req.DepartReq;
import com.gov.wiki.organization.req.query.DepartQuery;
import com.gov.wiki.organization.service.IDepartService;

@Service("departService")
public class DepartServiceImpl extends BaseServiceImpl<OrgDepart, String, OrgDepartDao> implements IDepartService {

	@Autowired
	private OrgDepartDao orgDepartDao;

	@Override
	public List<OrgDepart> findByParams(DepartQuery dq) {
		dq = dq == null?new DepartQuery():dq;
		String path = "";
		if(StringUtils.isBlank(dq.getKeywords())) {
			if(StringUtils.isNotBlank(dq.getParentId())) {
				path = this.queryDepartPath(dq.getParentId());
			}
			path = StringUtils.isBlank(path)?OrgDepart.ACCOUNT_PATH:path;
		}else {
			if(StringUtils.isNotBlank(dq.getParentId())) {
				path = this.queryDepartPath(dq.getParentId());
			}
		}
		List<OrgDepart> rsList = new ArrayList<OrgDepart>();
		DepartQuery param = dq;
		final String pathParam = path;
		Sort sort = Sort.by(Direction.ASC, "path");
		Specification<OrgDepart> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();
			if(StringUtils.isNotBlank(pathParam)) {
				predicate.getExpressions().add(criteriaBuilder.like(root.get("path").as(String.class), pathParam + "%"));
			}
			if(StringUtils.isNotBlank(param.getKeywords())) {
				predicate.getExpressions().add(criteriaBuilder.or(
					criteriaBuilder.like(root.get("name").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("code").as(String.class), "%" + param.getKeywords() + "%"),
					criteriaBuilder.like(root.get("shortName").as(String.class), "%" + param.getKeywords() + "%")
				));
			}
			return predicate;
		};
		List<OrgDepart> depList = this.baseRepository.findAll(spec, sort);
		if(StringUtils.isBlank(dq.getKeywords())) {
			for(OrgDepart d:depList) {
				if(d == null) {
					continue;
				}
				boolean flag = recursiveLookup(d, rsList);
				if(!flag) {
					rsList.add(d);
				}
			}
		}else {
			rsList.addAll(depList);
		}
		sortDepartBySortNo(rsList);
		return rsList;
	}
	
	private void sortDepartBySortNo(List<OrgDepart> rsList) {
		if(rsList == null || rsList.isEmpty()) {
			return;
		}
		rsList.sort(Comparator.comparing(OrgDepart :: getSortNo));
		for(OrgDepart d:rsList) {
			sortDepartBySortNo(d.getDepartList());
		}
	}
	
	/**
	 * @Title: recursiveLookup 
	 * @Description: 递归查找并赋值
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	private boolean recursiveLookup(OrgDepart depart, List<OrgDepart> list) {
		boolean flag = false;
		if(list.isEmpty()) {
			return flag;
		}
		for(OrgDepart p:list) {
			List<OrgDepart> childList = p.getDepartList();
			childList = childList == null?new ArrayList<OrgDepart>():childList;
			if(p.getId().equals(depart.getParentId())) {
				flag = true;
				childList.add(depart);
				break;
			}else {
				flag = recursiveLookup(depart, childList);
			}
		}
		return flag;
	}
	

	@Override
	public boolean checkDepartNameExist(OrgDepart depart) {
		boolean flag = false;
		final OrgDepart param = depart;
		List<OrgDepart> depList = this.baseRepository.findAll(new Specification<OrgDepart>() {
			@Override
			public Predicate toPredicate(Root<OrgDepart> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				query.where(criteriaBuilder.and(criteriaBuilder.equal(root.get("name").as(String.class), param.getName()),
					criteriaBuilder.equal(root.get("parentId").as(String.class), param.getParentId()),
						criteriaBuilder.equal(root.get("companyId").as(String.class),param.getCompanyId())
				));
				return null;
			}
		});
		if(depList != null && !depList.isEmpty()) {
			for(OrgDepart de:depList) {
				if(!de.getId().equals(depart.getId())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	@Override
	public List<String> getDepartIdsByPath(String path) {
		List<OrgDepart> dList = queryChildDepartByPath(path);
		List<String> ids = new ArrayList<String>();
		if(dList != null && !dList.isEmpty()) {
			for(OrgDepart d:dList) {
				if(d == null || StringUtils.isBlank(d.getId())) {
					continue;
				}
				ids.add(d.getId());
			}
		}
		return ids;
	}

	@Override
	public void batchDelByIds(List<String> ids) {
		this.baseRepository.batchDelByIds(new Date(), ids);
	}

	@Override
	public int getMaxSeq() {
		return this.baseRepository.queryMaxSeq();
	}

	@Override
	public String queryDepartPath(String departId) {
		OrgDepart d = this.findById(departId);
		return d != null?d.getPath():"";
	}

	@Override
	public List<OrgDepart> queryChildDepartByPath(String path) {
		if(StringUtils.isBlank(path)) {
			return null;
		}
		return this.baseRepository.queryChildByPath(path);
	}

	@Override
	public ResultBean<OrgDepart> saveOrUpdateDepart(ReqBean<DepartReq> bean) {
		DepartReq req = bean.getBody();
		OrgDepart depart = req == null?null:req.toEntity();
		CheckUtil.notNull(depart, ResultCode.COMMON_ERROR, "参数不存在！");
		CheckClass.check(depart);
		OrgDepart old = null;
		if(StringUtils.isNotBlank(depart.getId())) {
			old = findById(depart.getId());
		}
		if(depart.getStatus() == null) {
			depart.setStatus(StatusEnum.ENABLE.getValue());
		}
		OrgDepart parent = null;
		if(StringUtils.isNotBlank(depart.getParentId())) {
			parent = findById(depart.getParentId());
		}else {
			depart.setParentId("");
		}
		CheckUtil.check(!(parent != null && parent.getId().equals(depart.getId())), ResultCode.COMMON_ERROR, "单位父级不能是自己！");
		boolean flag = checkDepartNameExist(depart);
		CheckUtil.check(!flag, ResultCode.COMMON_ERROR, "同一层级存在相同的部门！");
//		List<OrgDepart> childList = new ArrayList<OrgDepart>();
//		if(old == null) {// 新增
//			int seq = getMaxSeq();
//			seq += 1;
//			depart.setSeq(seq);
//			if(parent == null) {
//				depart.setPath(LevelUtil.calculateLevel("", seq));
//			}else {
//				depart.setPath(LevelUtil.calculateLevel(parent.getPath(), seq));
//			}
//		}else {// 修改
//			if(!old.getParentId().equals(depart.getParentId())) {//修改部门父级,重新生成编号
//				String newPath = LevelUtil.calculateLevel(parent.getPath(), old.getSeq());
//				depart.setPath(newPath);
//				String oldPath = old.getPath();
//				List<OrgDepart> cList = queryChildDepartByPath(oldPath);
//				if(cList != null && !cList.isEmpty()) {
//					CheckUtil.check(!adjustRootDown(cList, depart.getParentId()), ResultCode.COMMON_ERROR, "不允许向该部门子节点调整！");
//					for(OrgDepart c:cList) {
//						if(!c.getId().equals(old.getId())) {
//							String cPath = c.getPath();
//							if(cPath.indexOf(oldPath) == 0){
//								cPath = newPath + cPath.substring(oldPath.length());
//								c.setPath(cPath);
//							}
//							childList.add(c);
//						}
//					}
//				}
//			}else {
//				depart.setSeq(old.getSeq());
//				depart.setPath(old.getPath());
//			}
//		}
		saveOrUpdate(depart);
//		if(!childList.isEmpty()) {
//			this.saveAll(childList);
//		}
		return new ResultBean(depart);
	}
	
	/**
	 * @Title: adjustRootDown 
	 * @Description: 同根部门向下调整，不支持
	 * @param 设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	private boolean adjustRootDown(List<OrgDepart> cList, String parentId) {
		boolean flag = false;
		if(cList == null || cList.isEmpty()) {
			return flag;
		}
		for(OrgDepart d:cList) {
			if(d == null) {
				continue;
			}
			if(d.getId().equals(parentId)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 查询当前用户所在部门及其子部门所有成员
	 */
	@Override
	public List<String> queryChildUsers(OrgMember member) {
		OrgDepart depart = member.getDepart();
		List<String> list = baseRepository.findUserIdByDepartPath(depart.getPath());
		return list;
	}

	@Override
	public List<OrgMember> queryChildUsersInfo(OrgMember member, int i) {
		OrgDepart depart = member.getDepart();
		String path = depart.getPath();
		if(i ==1 || i == 2) {
			path += "!";
		}
		List<OrgMember> list = baseRepository.findUserByDepartPath(path);
		if(i == 2) {
			list.add(0,member);
		}
		return list;
	}

	@Override
	public List<String> queryChildUsers(OrgMember member, int i) {
		OrgDepart depart = member.getDepart();
		String path = depart.getPath();
		if(i ==1 || i == 2) {
			path += "!";
		}
		List<String> list = baseRepository.findUserIdByDepartPath(path);
		if(i == 2) {
			list.add(0,member.getId());
		}
		return list;
	}
	
	@Override
	public List<String> queryChildUserByDepartIds(List<String> ids) {
		return baseRepository.findMemberIdInId(ids);
	}

	@Override
	public List<String> queryDepartRangeByUser(String userId) {
		List<String> pathList = new ArrayList<String>();
		List<String> rsList = this.baseRepository.queryPathRangeByUserId(userId);
		if(rsList != null && !rsList.isEmpty()) {
			for(String path:rsList) {
				if(StringUtils.isBlank(path)) {
					continue;
				}
				if(!hasPath(path, pathList)) {
					pathList.add(path);
				}
			}
		}
		return pathList;
	}
	
	/**
	 * @Title: hasPath 
	 * @Description: 判断是否包含path
	 * @param 设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	private boolean hasPath(String path, List<String> pathList) {
		boolean flag = false;
		if(StringUtils.isBlank(path)) {
			return flag;
		}
		for(int i=0;i < pathList.size(); i++) {
			String p = pathList.get(i);
			if(path.startsWith(p) || p.startsWith(path)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public ResultBean<List<OrgDepart>> queryChildListByRanges(ReqBean<List<String>> bean) {
		if(bean == null || bean.getBody() == null || bean.getBody().isEmpty()) {
			return new ResultBean<List<OrgDepart>>();
		}
		Sort sort = Sort.by(Direction.ASC, "path");
		Specification<OrgDepart> spec = (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.disjunction();
			List<String> paths = bean.getBody();
			for(String path:paths) {
				predicate.getExpressions().add(criteriaBuilder.like(root.get("path").as(String.class), path + "%"));
			}
			return predicate;
		};
		List<OrgDepart> depList = this.baseRepository.findAll(spec, sort);
		return new ResultBean<List<OrgDepart>>(depList);
	}

	@Override
	public List<Object[]> findfirstlevel(String companyid) {
		return orgDepartDao.findfirstlevel(companyid);
	}

	@Override
	public List<Object[]> findsecondlevel(String parentid) {
		return orgDepartDao.findsecondlevel(parentid);
	}

	@Override
	public List<Object[]> groupParentId(String companyid) {
		return this.baseRepository.groupParentId(companyid);
	}

	@Override
	public List<Object[]> findIdByParentId(String parentid,String companyid) {
		return this.baseRepository.findIdByParentId(parentid,companyid);
	}

	@Override
	public List<OrgDepart> findByCompanyId(String companyid) {
		return this.baseRepository.findByCompanyId(companyid);
	}

	@Override
	public List<OrgDepart> findIdByParentId(String parentid) {
		return this.baseRepository.findByParentId(parentid);
	}

	@Override
	public OrgDepart findByDirector(String director) {
		return this.baseRepository.findByDirector(director);
	}

	@Override
	public OrgDepart findByInChargeLeaderLike(String incharge) {
		return this.baseRepository.findByInChargeLeaderLike("%"+incharge+"%");
	}
}