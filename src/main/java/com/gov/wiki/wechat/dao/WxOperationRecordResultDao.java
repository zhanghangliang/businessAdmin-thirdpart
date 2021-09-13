/**
 * @Title: WxOperationRecordResultDao.java
 * @Package com.gov.wiki.wechat.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年12月16日
 * @version V1.0
 */
package com.gov.wiki.wechat.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.wechat.WxOperationRecordResult;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: WxOperationRecordResultDao
 * @Description: 微信用户操作记录结果信息管理DAO层接口
 * @author cys
 * @date 2020年12月16日
 */
@Repository
public interface WxOperationRecordResultDao extends BaseRepository<WxOperationRecordResult, String>{

	/**
	 * @Title: findByRecordIdOrderBySortNoAsc
	 * @Description: 根据记录ID查询详情
	 * @param recordId
	 * @return List<WxOperationRecordResult> 返回类型
	 * @throws
	 */
	List<WxOperationRecordResult> findByRecordIdOrderBySortNoAsc(String recordId);
	
	/**
	 * @Title: delByRecordIds
	 * @Description: 根据操作记录ID删除记录详情
	 * @param recordIds
	 * @return void 返回类型
	 * @throws
	 */
	@Transactional
	@Modifying
	@Query("delete from WxOperationRecordResult rr where rr.recordId in(:recordIds)")
	void delByRecordIds(@Param("recordIds") List<String> recordIds);
}
