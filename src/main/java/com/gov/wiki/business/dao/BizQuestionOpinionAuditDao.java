package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizQuestionOpinionAudit;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizQuestionOpinionAuditDao
 * @Description: 问题选项审批记录管理DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizQuestionOpinionAuditDao extends BaseRepository<BizQuestionOpinionAudit, String> {
	
}