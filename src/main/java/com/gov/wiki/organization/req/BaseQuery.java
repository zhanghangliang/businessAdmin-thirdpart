/**
 * @Title: BaseQuery.java 
 * @Package com.insolu.spm.organization.req 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author cys 
 * @date 2019年11月11日 下午5:04:28 
 * @version V1.0 
 */
package com.gov.wiki.organization.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: BaseQuery 
 * @Description: 查询基础参数
 * @author cys
 * @date 2019年11月11日 下午5:04:28
 */
@Data
@ApiModel(value = "BaseQuery", description = "查询基础参数")
public class BaseQuery {

	/**
	 * 关键字
	 */
	@ApiModelProperty(value = "关键字")
	private String keywords;
}
