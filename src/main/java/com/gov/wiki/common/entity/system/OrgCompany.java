package com.gov.wiki.common.entity.system;

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
@Table(name = "org_company")
@ApiModel(value = "OrgCompany", description = "公司对象")
@DynamicInsert
@Accessors(chain = true)
public class OrgCompany extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "companyName", value = "公司姓名")
    @Column(name = "company_name", nullable = true)
    private String companyName;
}
