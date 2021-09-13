/**
 * @Title: ControllerMonitor.java
 * @Package com.jade.filesystem.aspect
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月30日
 * @version V1.0
 */
package com.gov.wiki.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: ControllerMonitor
 * @Description: 控制类监视器
 * @author cys
 * @date 2019年7月30日
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerMonitor {

	/**
	 * @Title: description
	 * @Description: 日志描述
	 * @return String 返回类型
	 * @throws
	 */
	String description() default "";
	
	/**
	 * @Title: operType
	 * @Description: 操作类型: 1-查询，2-新增，3-修改，4-删除，5-登录，6-注册，7-注销，8-合并
	 * @return int 返回类型
	 * @throws
	 */
	int operType() default 1;
}