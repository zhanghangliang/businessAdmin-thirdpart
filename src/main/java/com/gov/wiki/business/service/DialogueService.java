package com.gov.wiki.business.service;

import com.gov.wiki.business.req.query.MonthQuery;
import com.gov.wiki.business.req.query.YearQuery;
import com.gov.wiki.business.res.AnnualDataRes;
import com.gov.wiki.business.res.RespondPromptlyRes;
import com.gov.wiki.common.service.IBaseService;
import com.gov.wiki.message.entity.McGroup;

public interface DialogueService extends IBaseService<McGroup, String> {

    /**
     * @param query:
     * @decription TODO(统计主动对话次数 - 统计人人对话次数)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/11 10:39
     */
    AnnualDataRes getActiveDialogue(YearQuery query);

    /**
     * @param query:
     * @decription TODO(统计人机对话次数)
     * @return: com.gov.wiki.business.res.AnnualDataRes
     * @author liusq
     * @date 2021/5/11 11:04
     */
    AnnualDataRes getManMachineDialogue(YearQuery query);

    /**
     * @param query:
     * @decription TODO(统计及时响应率)
     * @return: com.gov.wiki.business.res.RespondPromptlyRes
     * @author liusq
     * @mail 2301409946@qq.com
     * @date 2021/5/11 11:40
     * @version V1.0
     */
    RespondPromptlyRes getRespondPromptly(MonthQuery query);
}