package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysLog
 * @Description: 日志记录实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Table(name = "sys_log")
@Entity
@ApiModel(value = "SysLog", description = "日志信息对象")
public class SysLog extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*日志内容
	*/
	@ApiModelProperty(value = "日志内容")
	@Column(name = "log_content", nullable = true)
	private String logContent;
	
	/**
	*请求路径
	*/
	@ApiModelProperty(value = "请求路径")
	@Column(name = "request_url", nullable = true)
	private String requestUrl;
	
	/**
	*耗时
	*/
	@ApiModelProperty(value = "耗时")
	@Column(name = "cost_time", nullable = true)
	private Long costTime;
	
	/**
	*IP
	*/
	@ApiModelProperty(value = "IP")
	@Column(name = "ip", nullable = true)
	private String ip;
	
	/**
	*操作类型
	*/
	@ApiModelProperty(value = "操作类型")
	@Column(name = "operate_type", nullable = true)
	private Integer operateType;
	
	/**
	*操作类型描述
	*/
	@ApiModelProperty(value = "操作类型描述")
	@Column(name = "operate_type_desc", nullable = true)
	private String operateTypeDesc;
	
	/**
	*请求类型
	*/
	@ApiModelProperty(value = "请求类型")
	@Column(name = "request_type", nullable = true)
	private String requestType;
	
	/**
	*请求参数
	*/
	@ApiModelProperty(value = "请求参数")
	@Column(name = "request_param", nullable = true)
	private String requestParam;
	
	/**
	*日志类型（1登录日志，2操作日志，3定时日志）
	*/
	@ApiModelProperty(value = "日志类型（1登录日志，2操作日志，3定时日志）")
	@Column(name = "log_type", nullable = true)
	private Integer logType;
	
	/**
	*操作用户账号
	*/
	@ApiModelProperty(value = "操作用户ID")
	@Column(name = "user_id", nullable = true)
	private String userId;
	
	/**
	 * 操作用户账号
	 */
	@ApiModelProperty(value = "操作用户账号")
	@Formula("(select m.username from org_member m where m.id=user_id)")
	private String username;
	
	/**
	*请求java方法
	*/
	@ApiModelProperty(value = "请求方法")
	@Column(name = "method", nullable = true)
	private String method;
	
	/**
	 * 登录日志
	 */
	public static final int TYPE_LOGIN = 1;
	/**
	 * 操作日志
	 */
	public static final int TYPE_TIP = 2;
}