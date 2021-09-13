package com.gov.wiki.common.entity.buss;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "biz_work_config")
@ApiModel(value = "BizWorkConfig", description = "工作时间配置表")
@DynamicInsert
public class BizWorkConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8458163798831877039L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
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
     * 星期段，用1-7表示周一到周日，多个用‘,’分割
     */
    @ApiModelProperty(value = "星期段，用1-7表示周一到周日，多个用‘,’分割")
    @Column(name = "week_period")
    private String weekPeriod;

    /**
     * 时间段，24小时制，用小时来表示，多个时间段用‘,’分割
     */
    @ApiModelProperty(value = "时间段，24小时制，用小时来表示，多个时间段用‘,’分割")
    @Column(name = "period")
    private String period;


}
