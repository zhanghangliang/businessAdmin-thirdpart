/**
 * @Title: BaseEntity.java
 * @Package com.jade.filesystem.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月25日
 * @version V1.0
 */
package com.gov.wiki.common.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName: BaseEntity
 * @Description: 实体基类
 * @author cys
 * @date 2019年7月25日
 */
@Setter
@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends IdEntity{

	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
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
	 * 更新时间
	 */
	@Column(name = "update_time")
	@UpdateTimestamp
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "updateTime", value = "更新时间")
	@JsonIgnore
	private Date updateTime;
	
	/**
	*删除标志 0-未删除 1-删除
	*/
	@JsonIgnore
	@Column(name = "del_flag", nullable = true)
	private Boolean delFlag = false;
	
	/**
	*更新人
	*/
	@Column(name = "update_by", nullable = true)
	@ApiModelProperty(name = "updateBy", value = "更新人")
	@LastModifiedBy
	@JsonIgnore
	private String updateBy;
	
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
	@Formula("(select o.real_name from org_member o where o.id=create_by)")
	@JsonIgnore
	private String createName;
	
	/**
	*更新人姓名
	*/
	@ApiModelProperty(name = "updateName", value = "更新人姓名")
	@Formula("(select o.real_name from org_member o where o.id=create_by)")
	@JsonIgnore
	private String updateName;
}