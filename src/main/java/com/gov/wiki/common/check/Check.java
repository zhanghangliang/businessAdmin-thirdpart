package com.gov.wiki.common.check;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Check {

	/**
	 * 是否允许为空，注释在类上面，则校验类，注释在属性上面，校验属性
	 * @return
	 */
	boolean nullable() default false;
	
	/**
	 * @Title: title 
	 * @Description: 字段名称
	 * @param 设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	String title() default "";
}
