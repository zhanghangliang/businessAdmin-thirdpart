package com.gov.wiki.common.entity.buss;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.entity.BaseEntity;

import lombok.Data;
import lombok.experimental.Accessors;


/**
*
*/

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Accessors(chain = true)
@Where(clause = "del_flag != 1")
@Table(name = "biz_material_source")
@SQLDelete(sql = "update biz_material_source set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_material_source set del_flag=1 where id=?")
public class BizMaterialSource extends BaseEntity{
	
	
	/**
	*
	*/
	@Column(name = "material_key", nullable = true)
	private Integer materialKey;
	
	/**
	*
	*/
	@Column(name = "material_value", nullable = true ,length=255)
	private String materialValue;
}