package com.gov.wiki.common.entity.buss;


import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "biz_matter_depository_slave")
@ApiModel(value = "BizMatterAuditSlave", description = "事项从库")
public class BizMatterDepositorySlave extends BaseEntity {
    @ApiModelProperty(name = "matterDepositoryId", value = "事项库编号")
    @Column(name = "matter_depository_id")
    private String matterDepositoryId;
    @ApiModelProperty(name = "matterAuditId", value = "资料库编号")
    @Column(name = "material_depository_id")
    private String materialDepositoryId;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "material_depository_id", insertable = false, updatable = false)
    private BizMaterialDepository bizMaterialDepository;

    @ApiModelProperty(name = "number", value = "材料数量")
    @Column(name = "number")
    private Integer number;
    @ApiModelProperty(name = "inspect", value = "验证")
    @Column(name = "inspect")
    private Boolean inspect;
    @ApiModelProperty(name = "collect", value = "收取")
    @Column(name = "collect")
    private Integer collect;
    @ApiModelProperty(name = "necessity", value = "必要性")
    @Column(name = "necessity")
    private Boolean necessity;
}
