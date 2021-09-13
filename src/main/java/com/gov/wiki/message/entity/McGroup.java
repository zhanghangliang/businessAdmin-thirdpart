package com.gov.wiki.message.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.gov.wiki.common.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
*群组
*/

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Accessors(chain = true)
@Table(name = "mc_group")
public class McGroup extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3203825545394536027L;

	
	/**
	*群名称
	*/
	@Column(name = "name", nullable = true ,length=100)
	private String name;
	
	/**
	*群组状态  1.等待客服加入。
	*	2.正常聊天中。
	*	3.聊天关闭
	*
	*/
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	 * 评价
	 */
	@Column(name = "score", nullable = true)
	private Integer score;
	/**
	 * 评论
	 */
	@Column(name = "comment", nullable = true)
	private String comment;
	
	@OneToMany(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "group_id",insertable = false, updatable = false)
	private List<McGroupMember> members;
	
}