package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.buss.BizWorkTime;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface BizWorkTimeDao extends BaseRepository<BizWorkTime,String> {

}
