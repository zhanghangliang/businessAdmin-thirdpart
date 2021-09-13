package com.gov.wiki.business.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizOpinionMaterialAudit;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizOpinionMaterialAuditDao
 * @Description: 问题选项材料审批记录DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizOpinionMaterialAuditDao extends BaseRepository<BizOpinionMaterialAudit, String> {
	
	/**
	 * @Title: replaceMaterialReference
	 * @Description: 替换关联材料
	 * @param oldId
	 * @param newId
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query(value = "update biz_opinion_material_audit set material_id=:newId " + 
			"where material_id=:oldId and opinion_audit_id in(select o.id from biz_question_opinion_audit o " + 
			"left join biz_question_audit q on q.id=o.question_audit_id " + 
			"where q.status=0)", nativeQuery = true)
	void replaceMaterialReference(@Param("oldId") String oldId, @Param("newId") String newId);
}