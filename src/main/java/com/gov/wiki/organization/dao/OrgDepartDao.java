package com.gov.wiki.organization.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgMember;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface OrgDepartDao extends BaseRepository<OrgDepart, String> {
	/**
	 * @Title: batchDelByIds 
	 * @Description: 批量删除部门信息
	 * @param 设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query("update OrgDepart set delFlag=true,updateTime=:updateTime where id in(:ids)")
	void batchDelByIds(@Param("updateTime") Date updateTime, @Param("ids") List<String> ids);
	
	/**
	 * @Title: findByType 
	 * @Description: 根据类型查找信息
	 * @param 设定文件 
	 * @return List<OrgDepart>    返回类型 
	 * @throws
	 */
	List<OrgDepart> findByType(Integer type);
	
	/**
	 * @Title: countDepart 
	 * @Description: 统计部门数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	@Query("select count(d) from OrgDepart d")
	int countDepart();
	
	/**
	 * @Title: queryMaxSeq 
	 * @Description: 获取单位部门最大序号
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	@Query("select max(d.seq) from OrgDepart d")
	int queryMaxSeq();
	
	/**
	 * @Title: queryChildByPath 
	 * @Description: 根据长编码查询子部门
	 * @param 设定文件 
	 * @return List<OrgDepart>    返回类型 
	 * @throws
	 */
	@Query("select d from OrgDepart d where d.path like concat(:path, '%') order by d.path asc")
	List<OrgDepart> queryChildByPath(@Param("path") String path);
	
	@Query("select d from OrgDepart d where d.path like concat(:path, '%') order by d.path asc")
	List<OrgDepart> queryChildByPathAndSysScope(@Param("path") String path);
	
	
	/**
	 * @Title: findByParentId 
	 * @Description: 根据父ID查询子部门
	 * @param 设定文件 
	 * @return List<OrgDepart>    返回类型 
	 * @throws
	 */
	List<OrgDepart> findByParentId(String parentId);

	@Query("select a.id from OrgMember a where a.depart.path like concat(:path, '%')")
	List<String> findUserIdByDepartPath(@Param("path") String path);
	
	@Query("from OrgMember a where a.depart.path like concat(:path, '%')")
	List<OrgMember> findUserByDepartPath(@Param("path") String path);
	
	@Query("SELECT a.id from OrgMember a " + 
			"where a.depart.path like CONCAT(( " + 
			"SELECT c.path from OrgDepart c where c.id IN(?1) " + 
			"),'%')")
	List<String> findMemberIdInId(List<String> ids);
	
	/**
	 * @Title: queryPathRangeByUserId 
	 * @Description: 根据用户查询用户管辖部门长编码
	 * @param 设定文件 
	 * @return List<String>    返回类型 
	 * @throws
	 */
	@Query("select d.path from OrgDepart d "
			+ "where (d.inChargeLeader like concat('%', :userId, '%') or d.director=:userId) "
			+ "order by d.path asc")
	List<String> queryPathRangeByUserId(@Param("userId") String userId);

	@Query("select t.id,t.name from OrgDepart t where t.companyId=?1 and t.parentId='-1'")
	List<Object[]> findfirstlevel(String companyid);

	@Query("select t.id,t.name from OrgDepart t where t.parentId=?1")
	List<Object[]> findsecondlevel(String parentid);

	@Query("select t.parentId,t.name from OrgDepart t where t.parentId <> '-1' and t.companyId=?1 group by t.parentId")
	List<Object[]> groupParentId(String companyid);

	@Query("select t.id,t.name from OrgDepart t where t.parentId=?1 and t.companyId=?2")
	List<Object[]> findIdByParentId(String parentid,String companyid);

	List<OrgDepart> findByCompanyId(String companyid);

	OrgDepart findByDirector(String director);

	OrgDepart findByInChargeLeaderLike(String incharge);

}