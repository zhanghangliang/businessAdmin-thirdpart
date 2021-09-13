/**
 * @Title: SysConfigDao.java 
 * @Package com.gov.wiki.system.dao
 * @Description: 系统配置DAO
 * @author cys 
 * @date 2019年12月8日 下午4:15:53 
 * @version V1.0 
 */
package com.gov.wiki.system.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.system.SysConfig;
import com.gov.wiki.common.repository.BaseRepository;

@Repository
public interface SysConfigDao extends BaseRepository<SysConfig, String> {

}