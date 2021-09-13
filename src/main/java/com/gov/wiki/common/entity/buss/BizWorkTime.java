package com.gov.wiki.common.entity.buss;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "biz_work_time")
@ApiModel(value = "BizWorkTime", description = "系统工作日，和工作时间")
@DynamicInsert
public class BizWorkTime extends IdEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6122578253962742668L;


	/**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    @CreationTimestamp
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonIgnore
    private Date createTime;
    
    
    @Column(name = "dutyTime", updatable = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "dutyTime", value = "生效时间")
    @JsonIgnore
    private Date dutyTime;

    /**
     *创建人
     */
    @Column(name = "create_by", nullable = true,updatable = false)
    @ApiModelProperty(name = "createBy", value = "创建人")
    @CreatedBy
    @JsonIgnore
    private String createBy;

    /**
     *创建人姓名
     */
    @ApiModelProperty(name = "createName", value = "创建人姓名")
    @Formula("(select o.username from org_member o where o.id=create_by)")
    @JsonIgnore
    private String createName;

    /**
     * 状态，1上班，2休息
     */
    @ApiModelProperty(value = "状态，1上班，2休息")
    @Column(name = "status")
    private Integer status;

    /**
     * 时间段，24小时制，用小时来表示，多个时间段用‘,’分割
     */
    @ApiModelProperty(value = "时间段，24小时制，用小时来表示，多个时间段用‘,’分割")
    @Column(name = "period")
    private String period;
    

}
