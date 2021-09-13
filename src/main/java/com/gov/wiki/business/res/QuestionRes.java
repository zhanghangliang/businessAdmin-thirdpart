package com.gov.wiki.business.res;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.gov.wiki.common.entity.buss.BizQuestionOpinionAudit;
import com.gov.wiki.common.res.BaseRes;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 问题库数据返回
 * @author wangmingwei
 *
 */
@Data
public class QuestionRes extends BaseRes {
	/**
	*题目名称
	*/
	@ApiModelProperty(value = "题目名称")
	private String name;
	
	/**
	*描述
	*/
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*关联题库ID
	*/
	@ApiModelProperty(value = "关联题库ID")
	private String releaseId;
	
	/**
	*操作类型
	*/
	@ApiModelProperty(value = "操作类型")
	private Integer operType;
	
	@ApiModelProperty(value = "选择策略 1-串行 2-并行")
	private Integer strategy;
	/**
	*状态
	*/
	@ApiModelProperty(value = "状态")
	private Integer status;
	
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	/**
	 * 问题选项列表
	 */
	@ApiModelProperty(value = "问题选项列表")
	private List<BizQuestionOpinionAudit> opinionList;
	
	@ApiModelProperty(value = "版本号")	
	private String version;
	
	@ApiModelProperty(value = "修改次数")	
	private Integer modifyTimes;
	
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
	
	@ApiModelProperty(value = "操作原因")	
	private String operReason;
}