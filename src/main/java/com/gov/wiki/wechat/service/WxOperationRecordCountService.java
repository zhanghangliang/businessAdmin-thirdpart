package com.gov.wiki.wechat.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gov.wiki.common.entity.wechat.WxOperationRecordCount;
import com.gov.wiki.common.service.IBaseService;

public interface WxOperationRecordCountService extends IBaseService<WxOperationRecordCount, String> {
    WxOperationRecordCount findBySubjectId(String subjectid);

	Page<WxOperationRecordCount> findEffectiveAll(Pageable pageable);

	/**
	 * 在主题中增加i次
	 * @param i
	 * @param subjectId
	 */
	void addCountBySubjectId(int i, String subjectId);
}
