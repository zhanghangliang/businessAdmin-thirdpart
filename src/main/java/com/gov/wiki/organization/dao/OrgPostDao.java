package com.gov.wiki.organization.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface OrgPostDao extends BaseRepository<OrgPost, String> {
	/**
	 * @Title: countPost 
	 * @Description: 统计岗位数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	@Query("select count(g) from OrgPost g")
	int countPost();
}
