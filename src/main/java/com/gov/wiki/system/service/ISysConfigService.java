/**
 * @Title: ISysConfigService.java 
 * @Package com.gov.wiki.system.service
 * @Description: 系统信息配置Service接口
 * @author cys 
 * @date 2019年12月8日 下午4:17:04 
 * @version V1.0 
 */
package com.gov.wiki.system.service;

import com.gov.wiki.common.entity.system.SysConfig;
import com.gov.wiki.common.service.IBaseService;

public interface ISysConfigService extends IBaseService<SysConfig, String>{
	
	/**
	 * @Title: findOneConfig 
	 * @Description: 查询一条系统配置信息
	 * @param 设定文件 
	 * @return SysConfig    返回类型 
	 * @throws
	 */
	SysConfig findOneConfig(String id);

	String getCodeByMaterial(Integer body);
}