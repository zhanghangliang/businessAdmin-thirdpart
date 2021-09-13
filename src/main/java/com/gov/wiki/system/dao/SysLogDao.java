package com.gov.wiki.system.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.system.SysLog;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface SysLogDao extends BaseRepository<SysLog, String> {
	
}
