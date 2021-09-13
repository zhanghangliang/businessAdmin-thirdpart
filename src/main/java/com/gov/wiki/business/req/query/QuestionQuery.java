/**
 * @Title: QuestionQuery.java
 * @Package com.gov.wiki.business.req.query
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.req.query;

import java.util.List;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: QuestionQuery
 * @Description: 问题查询请求参数
 * @author cys
 * @date 2020年8月25日
 */
@Data
@ApiModel(value = "QuestionQuery", description = "问题查询请求参数")
public class QuestionQuery extends BaseQuery{

	@ApiModelProperty(value = "操作类型")
	private List<Integer> operTypeList;
	
	@ApiModelProperty(value = "状态")
	private List<Integer> statusList;
	
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
}
