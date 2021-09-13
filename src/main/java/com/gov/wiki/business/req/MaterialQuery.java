package com.gov.wiki.business.req;

import java.util.List;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: MaterialQuery
 * @Description: 资料查询请求对象
 * @author cys
 * @date 2020年11月24日
 */
@Data
@ApiModel(value = "MaterialQuery", description = "资料查询请求对象")
public class MaterialQuery extends BaseQuery{
	
    @ApiModelProperty(value = "证件类型")
    private List<Integer> typeList;
    
    @ApiModelProperty(value = "行政职责分类")	
	private String dutyType;
    
    @ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
    
    @ApiModelProperty(value = "状态参数")
	private List<Integer> statusList;
	
	@ApiModelProperty(value = "操作类型参数")
	private List<Integer> operTypeList;
	
	@ApiModelProperty(value = "资料来源")
	private String materialSource;
}
