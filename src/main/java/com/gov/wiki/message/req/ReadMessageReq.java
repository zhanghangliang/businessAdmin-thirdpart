package com.gov.wiki.message.req;

import com.gov.wiki.common.check.Check;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReadMessageReq {

	@ApiModelProperty(value = "组id")
	@Check
	private String groupId;
	
	@ApiModelProperty(value = "查询当前组的所有消息")
	private Boolean queryAll;
}
