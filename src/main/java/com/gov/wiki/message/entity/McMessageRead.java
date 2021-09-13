package com.gov.wiki.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.gov.wiki.common.entity.IdEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
*
*/

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Accessors(chain = true)
@Table(name = "mc_message_read")
public class McMessageRead extends IdEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4720024441213454947L;

	
	/**
	*消息id
	*/
	@Column(name = "message_id", nullable = false ,length=255)
	private String messageId;
	
	/**
	*阅读时间
	*/
	@Column(name = "read_time", nullable = false)
	private Date readTime;
	
	/**
	*阅读人
	*/
	@Column(name = "user_id", nullable = false ,length=32)
	private String userId;
}