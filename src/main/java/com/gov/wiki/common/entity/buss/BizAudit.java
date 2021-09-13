package com.gov.wiki.common.entity.buss;

import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "biz_audit")
@ApiModel(value = "BizAudit", description = "审核表")
public class BizAudit extends BaseEntity {
    @ApiModelProperty(name = "auditedId", value = "被审核编号")
    @Column(name = "audited_id")
    private String auditedId;
    @ApiModelProperty(name = "objectType", value = "资料or事项")
    @Column(name = "object_type")
    private Integer objectType;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "audit_id", referencedColumnName = "id")
    @Fetch(FetchMode.SUBSELECT)
    private List<BizAuditOpinion> bizAuditOpinions;
}
