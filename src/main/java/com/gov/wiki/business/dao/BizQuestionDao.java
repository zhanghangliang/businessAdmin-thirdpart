package com.gov.wiki.business.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizQuestion;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizQuestionDao
 * @Description: 问题管理DAO层级接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizQuestionDao extends BaseRepository<BizQuestion, String> {
	/**
	 * @Title: recoveryQuestion
	 * @Description: 回收题库信息
	 * @param questionId
	 * @return void 返回类型
	 * @throws
	 */
	@Modifying
	@Transactional
	@Query("update BizQuestion q set q.recyclingMark=true where q.id=:questionId")
	void recoveryQuestion(@Param("questionId") String questionId);
}