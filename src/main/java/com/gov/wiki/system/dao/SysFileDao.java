package com.gov.wiki.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.SysFile;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface SysFileDao extends BaseRepository<SysFile, String> {
	/**
	 * @Title: queryMaxSortNo
	 * @Description: 查询当前最大排序号
	 * @param referenceId
	 * @return int 返回类型
	 * @throws
	 */
	@Query("select (coalesce(max(f.sortNo), 0) + 1) from SysFile f where f.referenceId=:referenceId")
	int queryMaxSortNo(@Param("referenceId") String referenceId);
	
	/**
	 * @Title: findByReferenceId
	 * @Description: 根据关联ID查询文件列表
	 * @param referenceId
	 * @return List<SysFile> 返回类型
	 * @throws
	 */
	@Query("select e from SysFile e where e.referenceId=?1 ORDER BY e.createTime desc")
	List<SysFile> findByReferenceId(String referenceId);

	@Query("select e from SysFile e where e.referenceId in (?1)")
	List<SysFile> findByReferenceIds(List<String> referenceId);
	
	/**
	 * @Title: findByUniqueCode
	 * @Description: 根据文件唯一码，查询文件信息
	 * @param uniqueCode
	 * @return List<SysFile> 返回类型
	 * @throws
	 */
	List<SysFile> findByUniqueCode(String uniqueCode);



}
