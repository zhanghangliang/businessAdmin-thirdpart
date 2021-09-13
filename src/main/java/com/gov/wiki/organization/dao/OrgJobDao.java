package com.gov.wiki.organization.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface OrgJobDao extends BaseRepository<OrgJob, String> {
	
	/**
	 * @Title: countJob 
	 * @Description: 统计职务数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	@Query("select count(g) from OrgJob g")
	int countJob();
}
