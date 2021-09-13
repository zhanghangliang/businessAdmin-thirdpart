package com.gov.wiki.common.entity.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: SysFile
 * @Description: 文件记录实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@Entity
@Table(name = "sys_file")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update sys_file set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update sys_file set del_flag=1 where id=?")
@DynamicInsert
public class SysFile extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*关联ID
	*/
	@ApiModelProperty(value = "关联ID")
	@Column(name = "reference_id", nullable = true)
	private String referenceId;
	
	/**
	*文件存放路径
	*/
	@ApiModelProperty(value = "文件存放路径")
	@Column(name = "file_url", nullable = true)
	private String fileUrl;
	
	/**
	*文件后缀
	*/
	@ApiModelProperty(value = "文件后缀")
	@Column(name = "file_suffix", nullable = true)
	private String fileSuffix;
	
	/**
	*文件名
	*/
	@ApiModelProperty(value = "文件名")
	@Column(name = "file_name", nullable = true)
	private String fileName;
	
	/**
	*文件大小
	*/
	@ApiModelProperty(value = "文件大小")
	@Column(name = "file_size", nullable = true)
	private Long fileSize;
	
	/**
	*文件类型
	*/
	@ApiModelProperty(value = "文件类型")
	@Column(name = "mime_type", nullable = true)
	private String mimeType;
	
	/**
	*缩略图访问路径
	*/
	@ApiModelProperty(value = "缩略图访问路径")
	@Column(name = "thumbnail_url", nullable = true)
	private String thumbnailUrl;
	
	/**
	*排序号
	*/
	@ApiModelProperty(value = "排序号")
	@Column(name = "sort_no", nullable = true)
	private Integer sortNo;
	
	/**
	*文件别名
	*/
	@ApiModelProperty(value = "文件别名")
	@Column(name = "alias", nullable = true)
	private String alias;
	
	/**
	*文件md5码
	*/
	@JsonIgnore
	@ApiModelProperty(value = "文件md5码")
	@Column(name = "unique_code", nullable = true)
	private String uniqueCode;
}