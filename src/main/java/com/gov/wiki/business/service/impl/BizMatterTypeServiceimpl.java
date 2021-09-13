package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizMatterTypeDao;
import com.gov.wiki.business.service.BizMatterTypeService;
import com.gov.wiki.common.entity.buss.BizMatterType;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BizMatterTypeServiceimpl extends BaseServiceImpl<BizMatterType,String, BizMatterTypeDao> implements BizMatterTypeService {
    @Override
    public BizMatterType findByKey(Integer key) {
        return this.baseRepository.findByMatterKey(key);
    }
}
