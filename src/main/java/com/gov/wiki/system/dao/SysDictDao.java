package com.gov.wiki.system.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.system.SysDict;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface SysDictDao extends BaseRepository<SysDict, String> {

	List<SysDict> findByDictCode(String code);
}
