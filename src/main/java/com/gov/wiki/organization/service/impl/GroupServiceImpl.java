/**
 * @Title: GroupServiceImpl.java
 * @Package com.spm.system.service.impl
 * @Description: 组管理处理接口实现
 * @author cys
 * @date 2019年11月2日
 * @version V1.0
 */
package com.gov.wiki.organization.service.impl;

import org.springframework.stereotype.Service;
import com.gov.wiki.common.entity.system.OrgGroup;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.organization.dao.OrgGroupDao;
import com.gov.wiki.organization.service.IGroupService;

@Service("groupService")
public class GroupServiceImpl extends BaseServiceImpl<OrgGroup, String, OrgGroupDao> implements IGroupService {

	@Override
	public int countGroup() {
		return this.baseRepository.countGroup();
	}

}