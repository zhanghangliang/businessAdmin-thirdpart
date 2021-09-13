package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizMatterType;
import com.gov.wiki.common.repository.BaseRepository;

public interface BizMatterTypeDao extends BaseRepository<BizMatterType,String> {
    BizMatterType findByMatterKey(Integer key);
}
