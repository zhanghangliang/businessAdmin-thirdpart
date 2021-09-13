package com.gov.wiki.business.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubjectWxRes {

	@ApiModelProperty(value = "id")
	private String id;
	/**
	*名称
	*/
	@ApiModelProperty(value = "名称")
	private String name;
}
