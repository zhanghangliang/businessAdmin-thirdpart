/**
 * @Title: IGroupService.java
 * @Package com.spm.system.service
 * @Description: 组管理处理接口
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service;

import com.gov.wiki.common.entity.system.OrgGroup;
import com.gov.wiki.common.service.IBaseService;

public interface IGroupService extends IBaseService<OrgGroup, String> {
	/**
	 * @Title: countGroup 
	 * @Description: 统计组数量
	 * @param 设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	int countGroup();
}
