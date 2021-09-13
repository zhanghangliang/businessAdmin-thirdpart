package com.gov.wiki.business.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.buss.BizSubject;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface BizSubjectDao extends BaseRepository<BizSubject, String> {
	
	/**
	 * @Title: recoverySubject
	 * @Description: 回收主题信息
	 * @param subjectId
	 * @return void 返回类型
	 * @throws
	 */
	@Modifying
	@Transactional
	@Query("update BizSubject s set s.recyclingMark=true where s.id=:subjectId")
	void recoverySubject(@Param("subjectId") String subjectId);
}
