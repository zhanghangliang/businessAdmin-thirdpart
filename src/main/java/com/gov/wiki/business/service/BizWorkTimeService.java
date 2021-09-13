package com.gov.wiki.business.service;

import java.util.List;

import com.gov.wiki.business.req.query.WorkTimeQuery;
import com.gov.wiki.common.entity.buss.BizWorkTime;
import com.gov.wiki.common.service.IBaseService;

public interface BizWorkTimeService extends IBaseService<BizWorkTime, String> {

	List<BizWorkTime> list(WorkTimeQuery req);

}
