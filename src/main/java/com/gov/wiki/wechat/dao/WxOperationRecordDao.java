package com.gov.wiki.wechat.dao;

import com.gov.wiki.common.entity.wechat.WxOperationRecord;
import com.gov.wiki.common.repository.BaseRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WxOperationRecordDao extends BaseRepository<WxOperationRecord, String> {
	List<WxOperationRecord> findByMemberId(String memberId);

	void deleteByMemberId(String memberId);
	
	/**
	 * @Title: queryIdsByMemberId
	 * @Description: 查询人员操作记录ID
	 * @param memberId
	 * @return List<String> 返回类型
	 * @throws
	 */
	@Query("select r.id from WxOperationRecord r where r.memberId=:memberId")
	List<String> queryIdsByMemberId(@Param("memberId") String memberId);
}
