package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.*;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName: SysDictItem
 * @Description: 数据字典子项实体
 * @author cys
 * @date 2019年11月2日
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "sys_dict_item")
@Where(clause = "del_flag != 1")
@ApiModel(value = "SysDictItem", description = "数据字典项对象")
@SQLDelete(sql = "update sys_dict_item set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update sys_dict_item set del_flag=1 where id=?")
@DynamicInsert
public class SysDictItem extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*字典id
	*/
	@Check(nullable = false, title = "数据字典ID")
	@ApiModelProperty(name = "dictId", value = "字典id")
	@Column(name = "dict_id", nullable = true)
	private String dictId;
	
	/**
	*字典项文本
	*/
	@Check(nullable = false, title = "字典项文本")
	@ApiModelProperty(name = "itemText", value = "字典项文本")
	@Column(name = "item_text", nullable = true)
	private String itemText;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*状态（1启用 0不启用）
	*/
	@ApiModelProperty(name = "status", value = "状态（1启用 0不启用）")
	@Column(name = "status", nullable = true)
	private Integer status;
	
	/**
	*字典项值
	*/
	@ApiModelProperty(name = "itemValue", value = "字典项值")
	@Column(name = "item_value", nullable = true)
	private String itemValue;
	
	/**
	*排序
	*/
	@ApiModelProperty(name = "sortNo", value = "排序")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*数据字典值编号
	*/
	@Check(nullable = false, title = "数据字典值编号")
	@ApiModelProperty(name = "itemCode", value = "数据字典值编号")
	@Column(name = "item_code", nullable = true)
	private String itemCode;
}