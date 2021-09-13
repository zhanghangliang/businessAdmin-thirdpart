/**
 * @Title: BizWorkTaskDao.java
 * @Package com.gov.wiki.business.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizWorkTask;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizWorkTaskDao
 * @Description: 工作任务Dao层接口
 * @author cys
 * @date 2020年11月25日
 */
@Repository
public interface BizWorkTaskDao extends BaseRepository<BizWorkTask, String>{

}
