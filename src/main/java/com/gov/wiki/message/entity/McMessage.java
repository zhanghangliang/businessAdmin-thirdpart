package com.gov.wiki.message.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.IdEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
*
*/

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "mc_message")
public class McMessage extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 401338869663080617L;

	/**
	 * 组ID
	 */
	@Column(name = "group_id", nullable = false, length = 32)
	private String groupId;

	@OneToMany(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "id", referencedColumnName = "group_id", insertable = false, updatable = false)
	private List<McGroup> groups;

	/**
	 * 消息内容
	 */
	@Column(name = "content", nullable = false, length = 1000)
	private String content;

	/**
	 * 文件ID
	 */
	@Column(name = "folder_id", nullable = true, length = 32)
	private String folderId;

	/**
	 * 消息状态： 1已发送，2，已撤回
	 */
	@Column(name = "status", nullable = true)
	private Integer status = 1;
	

	/**
	*
	*/
	@Column(name = "link_message_id", nullable = true, length = 32)
	private String linkMessageId;
	/**
	 * 消息类型：1 文本消息 2 图片消息 3 视频 4 外连接 5 等待对话 6 关闭会话 7 客户加入会话 8 客服加入会话
	 */
	@Column(name = "message_type", nullable = true)
	private Integer messageType;

	/**
	 * 消息链接
	 */
	@Column(name = "message_url", nullable = true)
	private String messageUrl;
	
	/**
	 * 创建时间
	 */
	@Column(name = "create_time", updatable = false)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(name = "createTime", value = "创建时间")
	private Date createTime;
	
	/**
	*删除标志 0-未删除 1-删除
	*/
	@JsonIgnore
	@Column(name = "del_flag", nullable = true)
	private Boolean delFlag = false;
	
	
	/**
	*创建人
	*/
	@Column(name = "create_by", nullable = true,updatable = false)
	@ApiModelProperty(name = "createBy", value = "创建人")
	private String createBy;
	
	/**
	*创建人姓名
	*/
	@ApiModelProperty(name = "createName", value = "创建人姓名")
	@Formula("(select o.real_name from org_member o where o.id=create_by)")
	private String createName;
	
}