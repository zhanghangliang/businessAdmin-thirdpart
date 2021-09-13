/**
 * @Title: BaseEntity.java
 * @Package com.jade.filesystem.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2019年7月25日
 * @version V1.0
 */
package com.gov.wiki.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.gov.wiki.common.utils.StringUtils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: BaseEntity
 * @Description: 实体基类
 * @author cys
 * @date 2019年7月25日
 */
@Getter
@Setter
@MappedSuperclass
public class IdEntity implements Serializable{

	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdEntity other = (IdEntity) obj;
		if (StringUtils.isNotBlank(id)) {
			return id.equals(other.getId());
		} 
		return super.equals(obj);
	}
}