/**   
 * @Copyright:  成都北诺星科技有限公司  All rights reserved.Notice 官方网站：http://www.beinuoxing.com
 */
package com.gov.wiki.common.res;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseRes extends IdRes {

	/**
	*创建人
	*/
	@ApiModelProperty(name = "createBy", value = "创建人")
	private String createBy;
	/**
	 * 创建人姓名
	 */
	@ApiModelProperty(value = "创建人姓名")
	private String createName;
	
	/**
	 * 创建时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "createTime", value = "创建时间")
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "updateTime", value = "更新时间")
	private Date updateTime;
	
	
	/**
	*更新人
	*/
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	
	
	/**
	*更新人姓名
	*/
	@ApiModelProperty(value = "更新人姓名")
	private String updateName;
	
}
