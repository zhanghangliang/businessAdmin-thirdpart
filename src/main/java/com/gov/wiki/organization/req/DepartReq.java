package com.gov.wiki.organization.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.system.OrgDepart;
import com.gov.wiki.common.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DepartReq
 * @Description: 部门请求对象
 * @author cys
 * @date 2019年11月1日
 */
@Data
@ApiModel(value = "DepartReq", description = "部门请求对象")
public class DepartReq{
	
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	
	/**
	*部门分管领导
	*/
	@ApiModelProperty(name = "inChargeLeader", value = "部门分管领导")
	private String[] inChargeLeader;
	
	/**
	*部门主管
	*/
	@ApiModelProperty(name = "director", value = "部门主管")
	private String director;

	/**
	*部门名称
	*/
	@ApiModelProperty(name = "name", value = "部门名称")
	@Check(nullable = false, title = "部门名称")
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*状态 0-停用 1-启用
	*/
	@ApiModelProperty(name = "status", value = "状态 0-停用 1-启用，默认启用")
	private Integer status = StatusEnum.ENABLE.getValue();
	
	/**
	*上级部门
	*/
	@ApiModelProperty(name = "parentId", value = "上级部门ID")
	private String parentId="-1";
	
	/**
	*部门管理员
	*/
	@ApiModelProperty(name = "administrator", value = "部门管理员")
	private String administrator;
	
	/**
	*部门编码
	*/
	@ApiModelProperty(name = "code", value = "部门编码")
	private String code;
	
	/**
	*排序号
	*/
	@ApiModelProperty(name = "sortNo", value = "排序号")
	private Integer sortNo = 0;
	
	/**
	*简称
	*/
//	@ApiModelProperty(name = "shortName", value = "简称")
//	private String shortName;
	
	/**
	*类型 1-公司 2-部门,默认部门
	*/
	@ApiModelProperty(name = "type", value = "类型 1-公司 2-部门,默认部门")
	private Integer type = 2;

	@ApiModelProperty(name = "companyId", value = "公司编号")
	private String companyId;
	
	/**
	 * @Title: toEntity 
	 * @Description: 转实体
	 * @param 设定文件 
	 * @return OrgDepart    返回类型 
	 * @throws
	 */
	@JsonIgnore
	public OrgDepart toEntity() {
		OrgDepart d = new OrgDepart();
		d.setAdministrator(this.administrator);
		d.setCode(this.code);
		d.setDelFlag(false);
		d.setDescription(this.description);
		d.setDirector(this.director);
		d.setId(this.id);
		d.setInChargeLeader(String.join(",", this.inChargeLeader));
		d.setName(this.name);
		d.setParentId(this.parentId);
//		d.setShortName(this.shortName);
		d.setSortNo(this.sortNo);
		d.setStatus(this.status);
		d.setType(this.type);
		d.setCompanyId(this.companyId);
		return d;
	}
}