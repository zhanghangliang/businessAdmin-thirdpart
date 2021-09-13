/**
 * @Title: FileInfoReq.java
 * @Package com.gov.wiki.system.req
 * @Description: 文件信息请求参数
 * @author cys
 * @date 2020年4月5日
 * @version V1.0
 */
package com.gov.wiki.system.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FileInfoReq", description = "文件信息请求参数")
public class FileInfoReq {

	@ApiModelProperty(name = "fileId", value = "文件主键")
	private String fileId;
	
	@ApiModelProperty(name = "alias", value = "文件别名")
	private String alias;
}
