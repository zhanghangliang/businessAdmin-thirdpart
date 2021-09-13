package com.gov.wiki.organization.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.PrivResource;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface PrivResourceDao extends BaseRepository<PrivResource, String> {
	/**
	 * @Title: batchDelByIds 
	 * @Description: 批量删除资源信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query("update PrivResource set delFlag=true,updateTime=:updateTime where id in(:ids)")
	void batchDelByIds(@Param("updateTime") Date updateTime, @Param("ids") List<String> ids);
	
	@Query("SELECT p from PrivResource p LEFT JOIN PrivRoleResource r on p.id=r.resourceId where r.roleId IN(?1)")
	List<PrivResource> findByRoleIds(List<String> roles);

	@Query("select t.parentId,t.name from PrivResource t where t.parentId <> '-1' group by t.parentId")
	List<Object[]> groupParentId();

	@Query("select t.id,t.name from PrivResource t where t.parentId=?1")
	List<Object[]> findIdByParentId(String parentid);

	List<PrivResource> findByParentIdAndIdIn(String parentid,List<String> ids);

	@Query("select t from PrivResource t where t.id in (?1)")
	List<PrivResource> findids(List<String> ids);

	@Query("select t.parentId,t.name,t.icon,t.componentUrl,t.menuUrl,t.sortNo from PrivResource t where t.parentId <> '-1' and t.parentId in (?1) group by t.parentId")
	List<Object[]> groupParentId2(List<String> ids);

	@Query("select t.id,t.name,t.icon,t.componentUrl,t.menuUrl,t.sortNo from PrivResource t where t.parentId=?1 and t.id in (?2)")
	List<Object[]> findIdByParentId2(String parentid,List<String> ids);
}
