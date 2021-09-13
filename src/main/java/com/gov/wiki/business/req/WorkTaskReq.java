/**
 * @Title: WorkTaskReq.java
 * @Package com.gov.wiki.business.req
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.req;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.res.IdRes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName: WorkTaskReq
 * @Description: 工作任务请求参数
 * @author cys
 * @date 2020年11月25日
 */
@ApiModel(value = "WorkTaskReq", description = "工作任务请求参数")
public class WorkTaskReq extends IdRes{
	/**
	 * 任务名称
	 */
	@ApiModelProperty(value = "任务名称")
	@Check(nullable = false, title = "任务名称")
	private String name;
	
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	 * 任务附件
	 */
	@ApiModelProperty(value = "附件")
	private String annex;
}
