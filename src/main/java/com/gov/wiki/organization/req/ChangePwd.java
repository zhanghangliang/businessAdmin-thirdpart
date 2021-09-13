package com.gov.wiki.organization.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangePwd {

	@ApiModelProperty(value = "旧密码")
	private String oldPwd;
	@ApiModelProperty(value = "新密码")
	private String newPwd;
}
