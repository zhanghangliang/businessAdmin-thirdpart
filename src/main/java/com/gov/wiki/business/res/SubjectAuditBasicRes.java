package com.gov.wiki.business.res;

import com.gov.wiki.common.res.BaseRes;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Setter
@Getter
public class SubjectAuditBasicRes extends BaseRes {

    /**
     *名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "主题大类1-一事一次办 2-单一事项")
    private Integer majorCategory;


    /**
     *主题类型
     */
    @ApiModelProperty(value = "主题类型")
    private String subjectType;


    /**
     *关键描述
     */
    @ApiModelProperty(value = "关键描述")
    private String keyDescription;

    /**
     *描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     *状态
     */
    @Column(name = "status", nullable = true)
    @ApiModelProperty(value = "状态")
    private Integer status;



}
