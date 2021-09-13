/**
 * @Title: DictItemServiceImpl.java 
 * @Package com.xiangtong.service.impl 
 * @Description: 数据字典接口实现
 * @author cys 
 * @date 2019年11月12日 下午5:32:06 
 * @version V1.0 
 */
package com.gov.wiki.system.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.system.dao.SysDictItemDao;
import com.gov.wiki.system.service.IDictItemService;

@Service("dictItemService")
public class DictItemServiceImpl extends BaseServiceImpl<SysDictItem, String, SysDictItemDao> implements IDictItemService{

	@Override
	public SysDictItem queryByItemCode(String itemCode) {
		if(StringUtils.isBlank(itemCode)) {
			return null;
		}
		List<SysDictItem> list = this.baseRepository.findByItemCode(itemCode);
		return list != null && !list.isEmpty()?list.get(0):null;
	}

}
