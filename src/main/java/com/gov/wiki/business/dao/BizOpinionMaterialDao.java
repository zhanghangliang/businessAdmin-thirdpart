package com.gov.wiki.business.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizOpinionMaterial;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizOpinionMaterialDao
 * @Description: 问题选项材料管理DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizOpinionMaterialDao extends BaseRepository<BizOpinionMaterial, String> {
	/**
	 * @Title: replaceMaterialReference
	 * @Description: 替换选项材料
	 * @param oldId
	 * @param newId
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query(value = "update BizOpinionMaterial s set s.materialId=:newId where s.materialId=:oldId")
	void replaceMaterialReference(@Param("oldId") String oldId, @Param("newId") String newId);
}