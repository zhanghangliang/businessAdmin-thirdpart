package com.gov.wiki.wechat.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CheckCodeReq", description = "验证码校验请求对象")
public class CheckCodeReq {
    @ApiModelProperty(name = "moblie", value = "手机号")
    private String mobile;
    @ApiModelProperty(name = "code", value = "验证码")
    private String code;
}
