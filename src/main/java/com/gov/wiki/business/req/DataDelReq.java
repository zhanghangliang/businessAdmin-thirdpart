/**
 * @Title: DataDelReq.java
 * @Package com.gov.wiki.business.req
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月27日
 * @version V1.0
 */
package com.gov.wiki.business.req;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DataDelReq
 * @Description: 数据删除请求对象
 * @author cys
 * @date 2020年11月27日
 */
@Data
@ApiModel(value = "DataDelReq", description = "数据删除请求对象")
public class DataDelReq {

	@ApiModelProperty(value = "待删除数据ID")
	private List<String> delIds;
	
	@ApiModelProperty(value = "操作原因")
	private String reason;
}
