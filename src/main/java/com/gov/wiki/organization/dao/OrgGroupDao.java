package com.gov.wiki.organization.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.system.OrgGroup;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface OrgGroupDao extends BaseRepository<OrgGroup, String> {
	/**
	 * @Title: countGroup 
	 * @Description: 统计组数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	@Query("select count(g) from OrgGroup g ")
	int countGroup();
}
