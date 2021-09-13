package com.gov.wiki.common.entity.buss;

import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "biz_matter_type")
@ApiModel(value = "BizMatterType", description = "事项类型")
public class BizMatterType extends BaseEntity {
    @ApiModelProperty(name = "matterKey", value = "键")
    @Column(name = "matter_key")
    private Integer matterKey;

    @ApiModelProperty(name = "matterValue", value = "值")
    @Column(name = "matter_value")
    private String matterValue;

    @ApiModelProperty(name = "matterDescribe", value = "描述")
    @Column(name = "matter_describe")
    private String matterDescribe;
}
