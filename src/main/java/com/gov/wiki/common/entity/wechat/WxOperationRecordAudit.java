package com.gov.wiki.common.entity.wechat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.IdEntity;
import com.gov.wiki.common.entity.system.SysDictItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "wx_operation_record_audit")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update wx_operation_record_audit set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update wx_operation_record_audit set del_flag=1 where id=?")
@ApiModel(value = "WxOperationRecordAudit", description = "用户操作记录审核表")
@DynamicInsert
public class WxOperationRecordAudit extends IdEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5416047774103146059L;

	/**
     * 关联Id
     */
    @Column(name="record_id")
    @ApiModelProperty(value = "recordId")
    private String recordId;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    /**
     *删除标志 0-未删除 1-删除
     */
    @JsonIgnore
    @Column(name = "del_flag", nullable = true)
    private Boolean delFlag;

    /**
     *创建人
     */
    @Column(name = "create_by", nullable = true,updatable = false)
    @ApiModelProperty(name = "createBy", value = "创建人")
    @CreatedBy
    private String createBy;

    /**
     *创建人姓名
     */
    @ApiModelProperty(name = "createName", value = "创建人姓名")
    @Formula("(select o.username from org_member o where o.id=create_by)")
    private String createName;

    @ApiModelProperty(value = "审核状态")
    @Column(name = "status")
    private Integer status;
    @ApiModelProperty(value = "审核原因（审核不通过原因）")
    @Column(name = "reason")
    private String reason;

}
