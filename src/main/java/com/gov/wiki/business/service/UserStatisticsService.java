package com.gov.wiki.business.service;

import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.business.res.LiftRateRes;
import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.service.IBaseService;

public interface UserStatisticsService extends IBaseService<WxMember, String> {

    /**
     * @param query:
     * @decription TODO(统计用户提升率)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/10 10:28
     */
    LiftRateRes getUserLiftRate(YearQuery query);

    /**
     * @param query:
     * @decription TODO(统计实名注册)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/10 15:20
     * @version V1.0
     */
    AnnualDataRes getVerified(YearQuery query);

    /**
     * @param query:
     * @decription TODO(统计活跃用户)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/10 15:53
     * @version V1.0
     */
    AnnualDataRes getActiveUser(YearQuery query);
}