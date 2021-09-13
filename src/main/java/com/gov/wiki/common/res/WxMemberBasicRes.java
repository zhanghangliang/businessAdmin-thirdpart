package com.gov.wiki.common.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@ApiModel(value = "WxMemberBasicRes",description = "微信人员基础返回信息")
public class WxMemberBasicRes {
    @ApiModelProperty(name = "name", value = "用户名")
    private String name;

    @ApiModelProperty(name = "password", value = "密码")
    private String password;

    /**
     *性别 1-男 2-女
     */
    @ApiModelProperty(name = "sex", value = "性别 1-男 2-女")
    private Integer sex;

    @ApiModelProperty(name = "mobile", value = "手机号")
    private String mobile;

    @ApiModelProperty(name = "headimgurl", value = "头像地址")
    private String headimgurl;

    @ApiModelProperty(name = "openid", value = "微信号唯一标识")
    @Column(name = "openid", nullable = true)
    private String openid;

    @ApiModelProperty(name = "state", value = "账号状态")
    private String state;

    @ApiModelProperty(name = "realName", value = "真实姓名")
    private String realName;


    @ApiModelProperty(name = "idCard", value = "身份证号")
    private String idCard;
}
