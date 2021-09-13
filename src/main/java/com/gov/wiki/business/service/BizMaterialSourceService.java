package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizMaterialSource;
import com.gov.wiki.common.service.IBaseService;

public interface BizMaterialSourceService extends IBaseService<BizMaterialSource, String> {
    BizMaterialSource findByKey(Integer key);
}
