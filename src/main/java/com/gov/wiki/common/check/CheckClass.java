package com.gov.wiki.common.check;

import java.lang.reflect.Field;

import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckClass {

	public static void check(Object obj) {
		Class clazz = obj.getClass();
		Field[] fields = obj.getClass().getDeclaredFields();
		Check classCheck = (Check) clazz.getAnnotation(Check.class);
		if(classCheck != null && classCheck.nullable()) {
			return;
		}
		CheckUtil.notNull(obj, ResultCode.PARAM_NULL, clazz.getName());
		for (Field f : fields) {
			Check feildCheck = f.getAnnotation(Check.class);
			if(feildCheck != null && !feildCheck.nullable()) {
				f.setAccessible(true);
				Object param = null;
				try {
					param = f.get(obj);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error("error",e);
				}
				String title = StringUtils.isBlank(feildCheck.title())?f.getName():feildCheck.title();
				CheckUtil.notNull(param, ResultCode.PARAM_NULL, title);
			}
		}
	}

}
