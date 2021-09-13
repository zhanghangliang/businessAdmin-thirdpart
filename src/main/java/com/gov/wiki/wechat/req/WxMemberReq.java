package com.gov.wiki.wechat.req;

import com.gov.wiki.common.check.Check;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "WxMemberReq", description = "微信成员请求对象")
public class WxMemberReq {
    @ApiModelProperty(name = "id", value = "唯一标识")
    private String id;
    @ApiModelProperty(name = "name", value = "用户姓名")
    private String name;
    @ApiModelProperty(name = "password", value = "密码")
    private String password;
    @ApiModelProperty(name = "mobile", value = "手机号")
    @Check
    private String mobile;
    @ApiModelProperty(name = "sex", value = "性别")
    private Integer sex;
    @ApiModelProperty(name = "openId", value = "微信号唯一标识")
    private String openid;
    
    @ApiModelProperty(value = "身份证号")
    private String idCard;
    @ApiModelProperty(value = "真实姓名")
    private String realName;

}
