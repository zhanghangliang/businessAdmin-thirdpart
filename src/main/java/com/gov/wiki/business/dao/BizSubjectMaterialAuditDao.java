package com.gov.wiki.business.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizSubjectMaterialAudit;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizSubjectMaterialAuditDao
 * @Description: 主题材料管理审批记录DAO层级接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizSubjectMaterialAuditDao extends BaseRepository<BizSubjectMaterialAudit, String> {
	
	/**
	 * @Title: replaceMaterialReference
	 * @Description: 替换更新未审核主题资料信息
	 * @param oldId
	 * @param newId
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query(value = "update biz_subject_material_audit set material_id=:newId " + 
			"where material_id=:oldId and subject_audit_id in(select s.id from biz_subject_audit s where status=0)", nativeQuery = true)
	void replaceMaterialReference(@Param("oldId") String oldId, @Param("newId") String newId);
}