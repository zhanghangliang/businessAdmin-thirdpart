package com.gov.wiki.message.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupQuery {

	@ApiModelProperty(value = "查询我的聊天列表")
	private Boolean queryMyList;
	
	@ApiModelProperty(value = "查询可加入的聊天列表")
	private Boolean queryCanJoin;
}
