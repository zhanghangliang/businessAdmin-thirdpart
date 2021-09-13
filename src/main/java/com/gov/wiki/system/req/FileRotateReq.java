/**
 * @Title: FileRotateReq.java
 * @Package com.gov.wiki.system.req
 * @Description: 文件旋转请求参数
 * @author cys
 * @date 2020年4月16日
 * @version V1.0
 */
package com.gov.wiki.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FileRotateReq", description = "文件旋转请求参数")
public class FileRotateReq {

	/**
	 * 文件ID
	 */
	@ApiModelProperty(value = "文件ID")
	private String fileId;
	
	/**
	 * 顺时针旋转角度
	 */
	@ApiModelProperty(value = "顺时针旋转角度")
	private int rotate;
	
	/**
	 * 文件访问权限
	 */
	@ApiModelProperty(value = "文件访问权限，default：默认，private：私有，public-read：公共读，public-read-write：公共读写")
	private String accessControl;
}