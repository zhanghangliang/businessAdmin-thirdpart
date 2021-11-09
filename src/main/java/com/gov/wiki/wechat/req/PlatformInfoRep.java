package com.gov.wiki.wechat.req;

import com.gov.wiki.common.utils.RSAUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PlatformInfoRep", description = "平台信息请求")
public class PlatformInfoRep {

    @ApiModelProperty(name = "username", value = "用户名；指手机号；政务服务网通过手机号注册")
    private String username;

    @ApiModelProperty(name = "userPassword", value = "密码；SHA加密密码")
    private String userPassword;

    @ApiModelProperty(name = "uuid", value = "唯一标识")
    private String uuid;

    @ApiModelProperty(name = "realName", value = "真实姓名")
    private String realName;

    @ApiModelProperty(name = "sex", value = "性别")
    private Integer sex;

    @ApiModelProperty(name = "idNumber", value = "身份证")
    private String idNumber;
}
