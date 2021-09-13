package com.gov.wiki.organization.dao;

import java.util.List;

import com.gov.wiki.common.entity.system.PrivResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.OrgRole;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface OrgRoleDao extends BaseRepository<OrgRole, String> {

	@Query("SELECT r from OrgRole r LEFT JOIN PrivMemberRole p on r.id=p.roleId where p.memberId=?1")
	List<OrgRole> findByUserId(String userId);

	@Query("SELECT p.id,p.name,p.icon,p.componentUrl,p.menuUrl,p.sortNo from PrivResource p LEFT JOIN PrivRoleResource r on p.id=r.resourceId where r.roleId =?1 and p.parentId='-1'")
	List<Object[]> findtop(String roleid);
}
