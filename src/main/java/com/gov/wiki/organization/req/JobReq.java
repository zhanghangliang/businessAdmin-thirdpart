package com.gov.wiki.organization.req;

import com.gov.wiki.common.entity.system.OrgJob;
import com.gov.wiki.common.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: JobReq
 * @Description: 职务请求对象
 * @author cys
 * @date 2019年11月1日
 */
@Data
@ApiModel(value = "JobReq", description = "职务请求对象")
public class JobReq {
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo = 0;
	
	/**
	*职务级别编码
	*/
	@ApiModelProperty(name = "code", value = "职务级别编码")
	private String code;
	
	/**
	*职务级别名称
	*/
	@ApiModelProperty(name = "name", value = "职务级别名称")
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
	public OrgJob toEntity() {
		OrgJob j = new OrgJob();
		j.setCode(code);
		j.setDelFlag(false);
		j.setDescription(this.description);
		j.setId(this.id);
		j.setName(this.name);
		j.setSortNo(this.sortNo);
		j.setStatus(this.status);
		return j;
	}
}