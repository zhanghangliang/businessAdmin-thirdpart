package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizApprovalRecord;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizApprovalRecordDao
 * @Description: 审核结果记录管理DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizApprovalRecordDao extends BaseRepository<BizApprovalRecord, String> {
	
}