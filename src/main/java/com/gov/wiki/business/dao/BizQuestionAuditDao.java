package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizQuestionAudit;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizQuestionAuditDao
 * @Description: 问题审批记录管理DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizQuestionAuditDao extends BaseRepository<BizQuestionAudit, String> {
}