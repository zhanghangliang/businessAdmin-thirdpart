/**
 * @Title: IDictItemService.java 
 * @Package com.xiangtong.service 
 * @Description: 数据字典Service接口
 * @author cys 
 * @date 2019年11月12日 下午5:31:50 
 * @version V1.0 
 */
package com.gov.wiki.system.service;

import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.service.IBaseService;

public interface IDictItemService extends IBaseService<SysDictItem, String>{

	SysDictItem queryByItemCode(String itemCode);
}
