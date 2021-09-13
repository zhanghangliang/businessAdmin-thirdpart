/**
 * @Title: LogQuery.java 
 * @Package com.gov.wiki.system.req.query
 * @Description: 日志查询请求参数
 * @author cys 
 * @date 2019年12月3日 下午4:46:50 
 * @version V1.0 
 */
package com.gov.wiki.system.req.query;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "LogQuery", description = "日志查询参数")
public class LogQuery extends BaseQuery{

	/**
	 * 日志类型（1登录日志，2操作日志，3定时日志）
	 */
	@ApiModelProperty(value = "日志类型（1登录日志，2操作日志，3定时日志）")
	private Integer logType;
}
