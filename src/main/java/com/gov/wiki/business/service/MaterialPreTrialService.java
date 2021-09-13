package com.gov.wiki.business.service;

import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.service.IBaseService;

public interface MaterialPreTrialService extends IBaseService<WxOperationRecord, String> {

    /**
     * @param query:
     * @decription TODO(统计材料预审并发量)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/11 10:13
     */
    AnnualDataRes getMaterialPreTrial(YearQuery query);
}