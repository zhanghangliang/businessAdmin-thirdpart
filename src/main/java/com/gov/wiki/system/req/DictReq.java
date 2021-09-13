package com.gov.wiki.system.req;

import com.gov.wiki.common.entity.system.SysDict;
import com.gov.wiki.common.utils.JSONUtil;
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
@ApiModel(value = "DictReq", description = "数据字典请求对象")
public class DictReq{
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "主键")
	private String id;
	
	/**
	*字典名称
	*/
	@ApiModelProperty(name = "dictName", value = "字典名称")
	private String dictName;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*字典编码
	*/
	@ApiModelProperty(name = "dictCode", value = "字典编码")
	private String dictCode;
	
	public SysDict toEntity() {
		SysDict d = new SysDict();
		d.setDelFlag(false);
		d.setDescription(JSONUtil.strToNull(this.description));
		d.setDictCode(JSONUtil.strToNull(this.dictCode));
		d.setDictName(JSONUtil.strToNull(this.dictName));
		d.setId(JSONUtil.strToNull(this.id));
		return d;
	}
}