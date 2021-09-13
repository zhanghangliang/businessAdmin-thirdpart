package com.gov.wiki.organization.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.entity.system.OrgGroup;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OrgGroup
 * @Description: 组请求对象
 * @author cys
 * @date 2019年11月1日
 */
@Data
@ApiModel(value = "GroupReq", description = "组请求对象")
public class GroupReq{
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	

	/**
	*组成员(成员ID，英文逗号隔开)
	*/
	@ApiModelProperty(name = "member", value = "组成员(成员ID，英文逗号隔开)")
	private String member;
	
	/**
	*组领导
	*/
	@ApiModelProperty(name = "leader", value = "组领导")
	private String leader;
	
	/**
	*组名
	*/
	@ApiModelProperty(name = "name", value = "组名")
	private String name;
	
	/**
	*权限属性 1-公开组 2-私有组
	*/
	@ApiModelProperty(name = "authority", value = "权限属性 1-公开组 2-私有组")
	private Integer authority;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*公开范围
	*/
	@ApiModelProperty(name = "publicRange", value = "公开范围")
	private String publicRange;
	
	/**
	*组主管
	*/
	@ApiModelProperty(name = "director", value = "组主管")
	private String director;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo = 0;
	
	/**
	*组类型
	*/
	@ApiModelProperty(name = "groupType", value = "组类型")
	private String groupType;
	
	/**
	*所属范围
	*/
	@ApiModelProperty(name = "belong", value = "所属范围")
	private String belong;
	
	@JsonIgnore
	public OrgGroup toEntity() {
		OrgGroup g = new OrgGroup();
		g.setAuthority(this.authority);
		if(StringUtils.isNotBlank(this.belong)) {
			OrgDepart belongDe = new OrgDepart();
			belongDe.setId(this.belong);
			g.setBelong(belongDe);
		}
		g.setDescription(this.description);
		g.setDirector(this.director);
		g.setDelFlag(false);
		if(StringUtils.isNotBlank(this.groupType)) {
			SysDictItem groupTypeItem = new SysDictItem();
			groupTypeItem.setId(this.groupType);
			g.setGroupType(groupTypeItem);
		}
		g.setId(this.id);
		g.setLeader(this.leader);
		g.setMember(this.member);
		g.setName(this.name);
		g.setPublicRange(this.publicRange);
		g.setSortNo(this.sortNo);
		g.setStatus(this.status);
		return g;
	}
}