package com.gov.wiki.business.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.repository.BaseRepository;

public interface BizMaterialDepositoryDao extends BaseRepository<BizMaterialDepository,String> {
	/**
	 * @Title: recoveryMaterial
	 * @Description: 回收资料信息
	 * @param materialId
	 * @return void 返回类型
	 * @throws
	 */
	@Modifying
	@Transactional
	@Query("update BizMaterialDepository m set m.recyclingMark=true where m.id=:materialId")
	void recoveryMaterial(@Param("materialId") String materialId);
}
