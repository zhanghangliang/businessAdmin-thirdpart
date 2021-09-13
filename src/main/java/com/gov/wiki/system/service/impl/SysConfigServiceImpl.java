/**
 * @Title: SysConfigServiceImpl.java 
 * @Package com.gov.wiki.system.service.impl
 * @Description: 系统信息配置管理Service接口实现
 * @author cys 
 * @date 2019年12月8日 下午4:17:20 
 * @version V1.0 
 */
package com.gov.wiki.system.service.impl;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.entity.system.SysConfig;
import com.gov.wiki.common.enums.MaterialEnum.MaterialType;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.StringUtils;
import com.gov.wiki.system.dao.SysConfigDao;
import com.gov.wiki.system.service.ISysConfigService;

@Service("sysConfigService")
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig, String, SysConfigDao> implements ISysConfigService{

	@Override
	public SysConfig findOneConfig(String id) {
		SysConfig config = null;
		List<SysConfig> list = this.findAll();
		if(list != null && !list.isEmpty()) {
			if(StringUtils.isBlank(id)) {
				config = list.get(0);
			}else {
				for(SysConfig c:list) {
					if(c.getId().equals(id)) {
						config = c;
						break;
					}
				}
				if(config == null) {
					config = list.get(0);
				}
			}
		}
		return config;
	}

	@Override
	public String getCodeByMaterial(Integer key) {
		String pre = "material_code_";
		CheckUtil.notNull(key, ResultCode.PARAM_NULL, "事项类型");
		SysConfig config = this.findOneConfig(null);
		MaterialType type = MaterialType.getEnumByKey(key);
		CheckUtil.notNull(type, ResultCode.DATA_NOT_EXIST, "事项类型");
		String attrKey = pre+type.name();
		String value = config.getConfigAttr().get(attrKey);
		Integer code = 0;
		if(value != null) {
			code = Integer.valueOf(value)+1;
		}
		value = String.format("%07d",code);
		config.getConfigAttr().put(attrKey, value);
		this.baseRepository.saveAndFlush(config);
		return type.name()+"_"+value;
	}
}