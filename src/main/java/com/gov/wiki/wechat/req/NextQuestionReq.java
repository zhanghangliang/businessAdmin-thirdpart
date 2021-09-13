package com.gov.wiki.wechat.req;

import com.gov.wiki.common.check.Check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NextQuestionReq {

	@ApiModelProperty(value = "主题id，必填")
	@Check
	private String subjectId;
	@ApiModelProperty(value = "上一题关系id，如果没有则留空")
	private String lastQaRelationShipId;
	@ApiModelProperty(value = "上一题选择的选项id")
	private String opinionId;
}
