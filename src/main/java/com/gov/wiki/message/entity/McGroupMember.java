package com.gov.wiki.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;

import com.gov.wiki.common.entity.IdEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
*组成员
*/

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "mc_group_member")
public class 	McGroupMember extends IdEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7128327766809899849L;

	
	/**
	*组id
	*/
	@Column(name = "group_id", nullable = true ,length=32)
	private String groupId;
	
	/**
	*用户id
	*/
	@Column(name = "user_id", nullable = true ,length=255)
	private String userId;
	
	/**
	*角色类型 0普通人员
	*/
	@Column(name = "role_type", nullable = true)
	private Integer roleType;
	
	/**
	*创建时间
	*/
	@Column(name = "create_time", nullable = true)
	@CreationTimestamp
	private Date createTime;
	
	/**
	*创建人
	*/
	@Column(name = "create_by", nullable = true ,length=32)
	@CreatedBy
	private String createBy;
	
	/**
	*加入类型,0 自动加入
	*/
	@Column(name = "join_type", nullable = true)
	private Integer joinType;
	
	/**
	 * 参与人的头像
	 */
	@Column(name = "head_url", nullable = true)
	private String headUrl;
}