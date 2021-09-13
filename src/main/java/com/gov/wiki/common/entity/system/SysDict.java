package com.gov.wiki.common.entity.system;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysDict
 * @Description: 数据字典实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "sys_dict")
@ApiModel(value = "SysDict", description = "数据字典对象")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update sys_dict set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update sys_dict set del_flag=1 where id=?")
@DynamicInsert
public class SysDict extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*字典名称
	*/
	@Check(nullable = false, title = "字典名称")
	@ApiModelProperty(name = "dictName", value = "字典名称")
	@Column(name = "dict_name", nullable = true)
	private String dictName;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	@Column(name = "description", nullable = true)
	private String description;
	
	/**
	*字典编码
	*/
	@Check(nullable = false, title = "字典编码")
	@ApiModelProperty(name = "dictCode", value = "字典编码")
	@Column(name = "dict_code", nullable = true)
	private String dictCode;
	
	/**
	 *  字典项
	 */
	@ApiModelProperty(name = "items", value = "字典项")
	@OneToMany(cascade=CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name="dict_id",insertable=false,updatable=false)
	@OrderBy("sortNo asc")
	private Set<SysDictItem> items;
}