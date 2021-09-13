package com.gov.wiki.common.entity.buss;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Data
@Table(name = "biz_audit_opinion")
@ApiModel(value = "BizAuditOpinion", description = "审核意见表")
public class BizAuditOpinion extends BaseEntity {
    @ApiModelProperty(name = "auditedId", value = "审核表编号")
    @Column(name = "audit_id")
    private String auditId;
    @ApiModelProperty(name = "auditOpinion", value = "审核意见")
    @Column(name = "audit_opinion")
    private String auditOpinion;
}
