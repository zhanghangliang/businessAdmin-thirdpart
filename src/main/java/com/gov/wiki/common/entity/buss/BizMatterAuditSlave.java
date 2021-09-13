package com.gov.wiki.common.entity.buss;

import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Data
@Table(name = "biz_matter_audit_slave")
@ApiModel(value = "BizMatterAuditSlave", description = "事项审核从表")
public class BizMatterAuditSlave extends BaseEntity {
    @ApiModelProperty(name = "matterAuditId", value = "事项审核表编号")
    @Column(name = "matter_audit_id")
    private String matterAuditId;
    @ApiModelProperty(name = "matterAuditId", value = "资料库编号")
    @Column(name = "material_depository_id")
    private String materialDepositoryId;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "material_depository_id", insertable = false, updatable = false)
    private BizMaterialDepository bizMaterialDepository;

    @ApiModelProperty(name = "matterAuditId", value = "事项从库编号")
    @Column(name = "matter_depository_slave_id")
    private String matterDepositorySlaveId;
    @ApiModelProperty(name = "number", value = "材料数量")
    @Column(name = "number")
    private Integer number;
    @ApiModelProperty(name = "inspect", value = "验证")
    @Column(name = "inspect")
    private Boolean inspect;
    @ApiModelProperty(name = "collect", value = "收取类型")
    @Column(name = "collect")
    private Integer collect;
    @ApiModelProperty(name = "necessity", value = "必要性")
    @Column(name = "necessity")
    private Boolean necessity;

}
