package com.gov.wiki.common.entity.wechat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "wx_member")
@Where(clause = "del_flag != 1")
@ApiModel(value = "WxMember", description = "微信人员对象")
@DynamicInsert
public class WxMember extends BaseEntity {

    @ApiModelProperty(name = "name", value = "用户名")
    @Column(name = "name", nullable = true)
    private String name;

    @ApiModelProperty(name = "password", value = "密码")
    @JsonIgnore
    @Column(name = "password", nullable = true)
    private String password;

    /**
     *性别 1-男 2-女
     */
    @ApiModelProperty(name = "sex", value = "性别 1-男 2-女")
    @Column(name = "sex", nullable = true)
    private Integer sex;

    @ApiModelProperty(name = "mobile", value = "手机号")
    @Column(name = "mobile", nullable = true)
    private String mobile;

    @ApiModelProperty(name = "headimgurl", value = "头像地址")
    @Column(name = "headimgurl", nullable = true)
    private String headimgurl;

    @ApiModelProperty(name = "openid", value = "微信号唯一标识")
    @Column(name = "openid", nullable = true)
    private String openid;

    @ApiModelProperty(name = "state", value = "账号状态")
    @Column(name = "state", nullable = true)
    private String state;
    
    @ApiModelProperty(name = "realName", value = "真实姓名")
    @Column(name = "real_name", nullable = true)
    private String realName;
    
    
    @ApiModelProperty(name = "idCard", value = "身份证号")
    @Column(name = "id_card", nullable = true)
    private String idCard;

    
    
}
