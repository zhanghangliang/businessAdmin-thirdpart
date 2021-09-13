package com.gov.wiki.organization.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.system.OrgPost;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: PostReq
 * @Description: 岗位请求对象
 * @author cys
 * @date 2019年11月2日
 */
@Data
@ApiModel(value = "PostReq", description = "岗位请求对象")
public class PostReq {

	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	
	/**
	*岗位类别
	*/
	@ApiModelProperty(name = "category", value = "岗位类别")
	private String category;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*岗位名称
	*/
	@ApiModelProperty(name = "name", value = "岗位名称")
	private String name;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
	/**
	*岗位编码
	*/
	@ApiModelProperty(name = "code", value = "岗位编码")
	private String code;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo = 0;
	
	@JsonIgnore
	public OrgPost toEntity() {
		OrgPost p = new OrgPost();
		if(StringUtils.isNotBlank(this.category)) {
			SysDictItem categoryItem = new SysDictItem();
			categoryItem.setId(this.category);
			p.setCategory(categoryItem);
		}
		p.setCode(code);
		p.setDelFlag(false);
		p.setDescription(description);
		p.setId(this.id);
		p.setName(this.name);
		p.setSortNo(this.sortNo);
		p.setStatus(this.status);
		return p;
	}
}