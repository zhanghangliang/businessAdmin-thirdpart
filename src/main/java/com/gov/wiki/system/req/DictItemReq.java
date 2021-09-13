package com.gov.wiki.system.req;

import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.enums.StatusEnum;
import com.gov.wiki.common.utils.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName: DictItemReq
 * @Description: 数据字典子项实体
 * @author cys
 * @date 2019年11月2日
 */
@Data
@ApiModel(value = "DictItemReq", description = "数据字典项请求对象")
@Accessors(chain = true)
public class DictItemReq {
	/**
	 * 主键
	 */
	@ApiModelProperty(name = "id", value = "唯一标识")
	private String id;

	/**
	*字典id
	*/
	@ApiModelProperty(name = "dictId", value = "字典id")
	private String dictId;
	
	/**
	*字典项文本
	*/
	@ApiModelProperty(name = "itemText", value = "字典项文本")
	private String itemText;
	
	/**
	*描述
	*/
	@ApiModelProperty(name = "description", value = "描述")
	private String description;
	
	/**
	*状态（1启用 0不启用）
	*/
	@ApiModelProperty(name = "status", value = "状态（1启用 0不启用）")
	private Integer status = StatusEnum.ENABLE.getValue();
	
	/**
	*字典项值
	*/
	@ApiModelProperty(name = "itemValue", value = "字典项值")
	private String itemValue;
	
	/**
	*排序
	*/
	@ApiModelProperty(name = "sortNo", value = "排序")
	private Integer sortNo;
	
	/**
	*字典项值编码
	*/
	@ApiModelProperty(name = "itemCode", value = "字典项值编码")
	private String itemCode;
	
	public SysDictItem toEntity() {
		SysDictItem item = new SysDictItem();
		item.setDelFlag(false);
		item.setDescription(JSONUtil.strToNull(this.description));
		item.setDictId(JSONUtil.strToNull(this.dictId));
		item.setItemText(JSONUtil.strToNull(this.itemText));
		item.setItemValue(JSONUtil.strToNull(this.itemValue));
		item.setSortNo(this.sortNo);
		item.setStatus(this.getStatus());
		item.setId(JSONUtil.strToNull(this.id));
		item.setItemCode(JSONUtil.strToNull(this.itemCode));
		return item;
	}
}