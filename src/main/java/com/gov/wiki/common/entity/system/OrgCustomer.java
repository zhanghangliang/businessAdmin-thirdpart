package com.gov.wiki.common.entity.system;

import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "org_customer")
@ApiModel(value = "OrgCustomer", description = "用户对象")
@DynamicInsert
@Accessors(chain = true)
public class OrgCustomer extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "customerName", value = "用户姓名")
    @Column(name = "customer_name", nullable = true)
    private String customerName;

    @ApiModelProperty(name = "customerPhone", value = "手机号")
    @Column(name = "customer_phone", nullable = true)
    private String customerPhone;

    @ApiModelProperty(name = "wechatNumber", value = "微信号")
    @Column(name = "wechat_number", nullable = true)
    private String wechatNumber;

    @ApiModelProperty(name = "sortNumber", value = "排序号")
    @Column(name = "sort_number", nullable = true)
    private String sortNumber;

    @ApiModelProperty(name = "state", value = "账号状态")
    @Column(name = "state", nullable = true)
    private Boolean state;
}
