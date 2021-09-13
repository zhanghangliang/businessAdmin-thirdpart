package com.gov.wiki.organization.dao;

import java.util.List;

import com.gov.wiki.common.entity.MemberInfoReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface OrgMemberDao extends BaseRepository<OrgMember, String> {

	@Query("select new com.gov.wiki.common.entity.MemberInfoReq(t.id,t.realName,t.companyId) from OrgMember t where t.depart.id=?1")
	List<MemberInfoReq> findByDepartId(String id);

	OrgMember getByUsername(String username);

	/**
	 * @Title: findByUsername 
	 * @Description: 根据人员登录名称查询人员信息
	 * @param 设定文件 
	 * @return List<OrgMember>    返回类型 
	 * @throwsh
	 */
	List<OrgMember> findByUsername(String username);
	
	/**
	 * @Title: countMember
	 * @Description: 统计人员数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	@Query("select count(g) from OrgMember g ")
	int countMember();
	
	/**
	 * @Title: findMemberByRoleId 
	 * @Description: 分页查询角色人员信息
	 * @param 设定文件 
	 * @return List<OrgMember>    返回类型 
	 * @throws
	 */
	@Query("select m from OrgMember m left join PrivMemberRole pr on m.id=pr.memberId where pr.roleId=:roleId and m.createBy in (:creater)")
	Page<OrgMember> findMemberByRoleId(@Param("roleId") String roleId, Pageable pageable,@Param("creater") List<String> creater);

	@Query("select new com.gov.wiki.common.entity.MemberInfoReq(m.id,m.realName,m.companyId) from OrgMember m where m.id IN(:ids)")
	List<MemberInfoReq> findNameByIds(@Param("ids") List<String> ids);

	@Query("select new com.gov.wiki.common.entity.MemberInfoReq(m.id,m.realName,m.companyId) from OrgMember m where m.companyId =?1")
	List<MemberInfoReq> getAllmember(String companyId);

	@Query("select m.id from OrgMember m where m.companyId=?1")
	List<String> findId(String companyId);
}