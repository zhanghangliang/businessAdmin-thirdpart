/**
 * @Title: SimpleSubjectRes.java
 * @Package com.gov.wiki.business.res
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年12月17日
 * @version V1.0
 */
package com.gov.wiki.common.buss.vo;

import com.gov.wiki.common.res.IdRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SimpleSubjectRes
 * @Description: 简单主题结果对象
 * @author cys
 * @date 2020年12月17日
 */
@Data
@ApiModel(value = "SimpleSubjectRes", description = "简单主题结果对象")
public class SimpleSubjectRes extends IdRes{
	/**
	*名称
	*/
	@ApiModelProperty(value = "主题名称")
	private String name;
	
	/**
	*关键描述
	*/
	@ApiModelProperty(value = "关键描述")
	private String keyDescription;
	
	/**
	*描述
	*/
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*主题类型
	*/
	@ApiModelProperty(value = "主题类型")
	private String subjectType;
	
	/**
	*所属地
	*/
	@ApiModelProperty(value = "所属地")
	private String territory;
	
	/**
	*办理流程
	*/
	@ApiModelProperty(value = "办理流程")
	private String processFlow;
	
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	/**
	 * 修改次数
	 */
	@ApiModelProperty(value = "修改次数")	
	private Integer modifyTimes;
	
	/**
	 * 版本号
	 */
	@ApiModelProperty(value = "版本号")	
	private String version;
	
	/**
	 * 回收标志
	 */
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
	
	@ApiModelProperty(value = "主题大类1-一事一次办 2-单一事项")
	private Integer majorCategory;
}
