package com.gov.wiki.business.req;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.gov.wiki.common.entity.buss.BizSubjectMaterialAudit;
import com.gov.wiki.common.entity.buss.BizSubjectQaRelationshipAudit;
import com.gov.wiki.common.res.BaseRes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 主题返回对象
 * @author wangmingwei
 *
 */
@Data
@ApiModel(value = "SubjectRes", description = "主题结果对象")
public class SubjectRes extends BaseRes {
	/**
	*名称
	*/
	@ApiModelProperty(value = "名称")
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
	
	@ApiModelProperty(value = "关联id")
	private String releaseId;
	
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
	
	/**
	*状态
	*/
	@ApiModelProperty(value = "状态")
	private Integer status;
	
	/**
	*操作类型
	*/
	@ApiModelProperty(value = "操作类型")
	private Integer operType;
	
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	@ApiModelProperty(value = "主题材料列表")
	private List<BizSubjectMaterialAudit> materials;
	
	@ApiModelProperty(value = "主题问题关系清单列表")
	private List<BizSubjectQaRelationshipAudit> relationShips;
	
	@ApiModelProperty(value = "版本号")	
	private String version;
	
	@ApiModelProperty(value = "修改次数")	
	private Integer modifyTimes;
	
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
	
	@ApiModelProperty(value = "操作原因")	
	private String operReason;
	
	@ApiModelProperty(value = "主题大类1-一事一次办 2-单一事项")
	private Integer majorCategory;
}