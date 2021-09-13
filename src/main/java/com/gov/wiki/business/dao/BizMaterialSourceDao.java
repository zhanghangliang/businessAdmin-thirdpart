package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.BizMaterialSource;
import com.gov.wiki.common.repository.BaseRepository;

public interface BizMaterialSourceDao extends BaseRepository<BizMaterialSource,String> {
    BizMaterialSource findByMaterialKey(Integer key);
}
