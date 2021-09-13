package com.gov.wiki.business.dao;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gov.wiki.common.entity.buss.BizStudy;
import com.gov.wiki.common.repository.BaseRepository;

public interface BizStudyDao extends BaseRepository<BizStudy, String> {
	/**
	 * @Title: queryMaxSeq 
	 * @Description: 获取学习资料树最大序号 
	 * @param 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	@Query(value = "select IFNULL(max(d.seq),0) from biz_study d", nativeQuery = true)
	int queryMaxSeq();
	
	/**
	 * @Title: queryChildByPath 
	 * @Description: 根据长编码查询子学习资料
	 * @param 设定文件 
	 * @return List<BizStudy>    返回类型 
	 * @throws
	 */
	@Query("select d from BizStudy d where d.path like concat(:path, '%') order by d.path asc")
	List<BizStudy> queryChildByPath(@Param("path") String path);
}
