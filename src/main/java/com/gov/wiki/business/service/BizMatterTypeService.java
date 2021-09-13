package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.BizMatterType;
import com.gov.wiki.common.service.IBaseService;

public interface BizMatterTypeService extends IBaseService<BizMatterType, String> {
    BizMatterType findByKey(Integer key);
}
