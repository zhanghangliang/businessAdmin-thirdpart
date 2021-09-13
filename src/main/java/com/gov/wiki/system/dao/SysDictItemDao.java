package com.gov.wiki.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface SysDictItemDao extends BaseRepository<SysDictItem, String> {
	
	/**
	 * @Title: findByItemCode
	 * @Description: 根据数据字典值编号查询数据字典值信息
	 * @param itemCode
	 * @return List<SysDictItem> 返回类型
	 * @throws
	 */
	List<SysDictItem> findByItemCode(String itemCode);
}
