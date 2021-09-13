package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.BizMaterialSourceDao;
import com.gov.wiki.business.service.BizMaterialSourceService;
import com.gov.wiki.common.entity.buss.BizMaterialSource;
import com.gov.wiki.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BizMaterialSourceServiceimpl extends BaseServiceImpl<BizMaterialSource,String, BizMaterialSourceDao> implements BizMaterialSourceService {
    @Override
    public BizMaterialSource findByKey(Integer key) {
        return this.baseRepository.findByMaterialKey(key);
    }
}
