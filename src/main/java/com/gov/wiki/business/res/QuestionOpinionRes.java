package com.gov.wiki.business.res;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.gov.wiki.common.entity.buss.BizOpinionMaterialAudit;
import com.gov.wiki.common.res.BaseRes;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 问题选项
 * @author wangmingwei
 *
 */
@Data
public class QuestionOpinionRes extends BaseRes {
	/**
	*选项名称
	*/
	@ApiModelProperty(value = "选项名称")
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*选择策略 1-串行 2-并行
	*/
	@ApiModelProperty(value = "选择策略 1-串行 2-并行")
	private Integer strategy;
	
	/**
	*办理部门
	*/
	@ApiModelProperty(value = "办理部门")
	private String handleDepart;
	
	/**
	*承诺时限
	*/
	@ApiModelProperty(value = "承诺时限")
	private String commitmentTime;
	
	/**
	*所属地
	*/
	@ApiModelProperty(value = "所属地")
	private String territory;
	
	/**
	*关联问题库选项ID
	*/
	@ApiModelProperty(value = "关联问题库选项ID")
	private String opinionId;
	
	/**
	*问题审核表ID
	*/
	@ApiModelProperty(value = "问题审核表ID")
	private String questionAuditId;
	
	/**
	*排序号
	*/
	@ApiModelProperty(value = "排序号")
	private Integer sortNo;
	
	/**
	*题库ID
	*/
	@ApiModelProperty(value = "题库ID")
	private String questionId;
	
	/**
	 * 问题选项材料列表
	 */
	@ApiModelProperty(value = "问题选项材料列表")
	private Set<BizOpinionMaterialAudit> materialList;
	
	/**
	*是否强制提醒 0-否 1-是
	*/
	@ApiModelProperty(value = "是否强制提醒 0-否 1-是")
	private Boolean forceRemind;
	
	/**
	*强制提醒内容
	*/
	@ApiModelProperty(value = "强制提醒内容")
	private String remindContent;
}