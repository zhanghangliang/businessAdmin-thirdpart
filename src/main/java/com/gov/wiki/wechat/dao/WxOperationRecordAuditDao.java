package com.gov.wiki.wechat.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gov.wiki.common.entity.wechat.WxMember;
import com.gov.wiki.common.entity.wechat.WxOperationRecordAudit;
import com.gov.wiki.common.repository.BaseRepository;

public interface WxOperationRecordAuditDao extends BaseRepository<WxOperationRecordAudit, String> {

	@Query("from WxOperationRecordAudit where recordId = :recordId order by createTime asc")
	List<WxOperationRecordAudit> findByRecordIdOrderByCreateTimeAsc(@Param("recordId")String recordId);
}
