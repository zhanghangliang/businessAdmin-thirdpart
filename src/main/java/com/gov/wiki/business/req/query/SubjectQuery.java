package com.gov.wiki.business.req.query;

import java.util.List;
import com.gov.wiki.organization.req.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SubjectQuery", description = "主题查询请求对象")
public class SubjectQuery extends BaseQuery {

	@ApiModelProperty(value = "主题类型")
	private String subjectType;
	
	@ApiModelProperty(value = "状态参数")
	private List<Integer> statusList;
	
	@ApiModelProperty(value = "操作类型参数")
	private List<Integer> operTypeList;
	
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
	
	@ApiModelProperty(value = "主题大类1-一事一次办 2-单一事项")
	private List<Integer> majorCategorys;
}
