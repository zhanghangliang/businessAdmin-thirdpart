package com.gov.wiki.common.entity.buss;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
*审批记录
*/
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_approval_record")
@SQLDelete(sql = "update biz_approval_record set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_approval_record set del_flag=1 where id=?")
@ApiModel(value = "BizApprovalRecord", description = "审批记录")
public class BizApprovalRecord extends BaseEntity{
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*审批时间
	*/
	@Column(name = "approval_time", nullable = true)
	@ApiModelProperty(value = "审批时间")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approvalTime;
	
	/**
	*审批人
	*/
	@Column(name = "approver", nullable = true)
	@ApiModelProperty(value = "审批人")
	private String approver;
	
	/**
	*审批意见
	*/
	@Column(name = "opinion", nullable = true)
	@ApiModelProperty(value = "审批意见")
	private String opinion;
	
	/**
	*审批结果状态 1-同意 2-拒绝
	*/
	@Column(name = "result", nullable = true)
	@ApiModelProperty(value = "审批结果状态")
	private Integer result;
	
	/**
	*备注
	*/
	@Column(name = "remarks", nullable = true)
	@ApiModelProperty(value = "备注")
	private String remarks;
	
	/**
	*操作对象ID
	*/
	@Column(name = "object_id", nullable = true)
	@ApiModelProperty(value = "操作对象ID")
	private String objectId;
}