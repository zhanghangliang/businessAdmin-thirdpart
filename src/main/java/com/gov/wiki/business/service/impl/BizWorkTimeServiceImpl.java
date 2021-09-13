package com.gov.wiki.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.business.dao.BizWorkTimeDao;
import com.gov.wiki.business.req.query.WorkTimeQuery;
import com.gov.wiki.business.service.BizWorkTimeService;
import com.gov.wiki.common.entity.buss.BizWorkTime;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.DateUtils;

@Service
public class BizWorkTimeServiceImpl
		extends BaseServiceImpl<BizWorkTime, String, BizWorkTimeDao>
		implements BizWorkTimeService {

	@Override
	public List<BizWorkTime> list(WorkTimeQuery req) {
		PredicateBuilder<BizWorkTime> builder = Specifications.and();
		if(req.getQueryMonth() != null) {
			Date[] monthRange = DateUtils.getRangeByMongth(req.getQueryMonth());
			builder.between("dutyTime", monthRange[0], monthRange[1]);
		}
		if(req.getQueryDay() != null) {
			Date[] monthRange = DateUtils.getRangeByDay(req.getQueryDay());
			builder.between("dutyTime", monthRange[0], monthRange[1]);			
		}
		return this.baseRepository.findAll(builder.build());
	}


}
