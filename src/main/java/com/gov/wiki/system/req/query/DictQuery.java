/**
 * @Title: DictQuery.java 
 * @Package com.xiangtong.req.query 
 * @Description: 数据字典查询参数
 * @author cys 
 * @date 2019年11月12日 下午4:50:40 
 * @version V1.0 
 */
package com.gov.wiki.system.req.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DictQuery", description = "数据字典查询参数")
public class DictQuery {
	/**
	 * 关键字
	 */
	@ApiModelProperty(value = "关键字")
	private String keywords;
}
