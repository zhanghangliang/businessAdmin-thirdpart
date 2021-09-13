/**
 * @Title: SysConfig.java 
 * @Package com.common.entity.system 
 * @Description: 系统配置对象
 * @author cys 
 * @date 2019年12月8日 下午4:02:40 
 * @version V1.0 
 */
package com.gov.wiki.common.entity.system;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name = "sys_config")
@ApiModel(value = "SysConfig", description = "系统配置对象")
@Where(clause = "del_flag != 1")
@DynamicInsert
public class SysConfig extends BaseEntity{
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */
	private static final long serialVersionUID = 1L;

	/**
	*系统中文名称
	*/
	@Check(nullable = false, title = "系统中文名称")
	@ApiModelProperty(name = "cnName", value = "系统中文名称")
	@Column(name = "cn_name", nullable = true)
	private String cnName;
	
	/**
	*系统英文名称
	*/
	@Check(nullable = false, title = "系统英文名称")
	@ApiModelProperty(name = "enName", value = "系统英文名称")
	@Column(name = "en_name", nullable = true)
	private String enName;
	
	/**
	*轮播图
	*/
	@ApiModelProperty(name = "carousel", value = "轮播图")
	@Column(name = "carousel", nullable = true)
	private String carousel;
	
	/**
	*轮播图文件列表
	*/
	@ApiModelProperty(name = "carouselList", value = "轮播图文件列表")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "reference_id", referencedColumnName = "carousel", insertable = false, updatable = false)
	@OrderBy("sort_no ASC")
	private List<SysFile> carouselList;
	
	/**
	*系统LOGO
	*/
	@ApiModelProperty(name = "logo", value = "系统LOGO")
	@Column(name = "logo", nullable = true)
	private String logo;
	
	/**
	*系统LOGO文件列表
	*/
	@ApiModelProperty(name = "logoList", value = "系统LOGO文件列表")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "reference_id", referencedColumnName = "logo", insertable = false, updatable = false)
	@OrderBy("sort_no ASC")
	private List<SysFile> logoList;
	
	/**
	*备注
	*/
	@ApiModelProperty(name = "remarks", value = "备注")
	@Column(name = "remarks", nullable = true)
	private String remarks;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="attr_key")
    @Column(name="attr_value")
    @CollectionTable(name="sys_config_attr",joinColumns={@JoinColumn(name="config_id",referencedColumnName = "id")})
    private Map<String, String> configAttr;
}