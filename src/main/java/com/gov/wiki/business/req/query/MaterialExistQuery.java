/**
 * @Title: MaterialExistQuery.java
 * @Package com.gov.wiki.business.req.query
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年11月25日
 * @version V1.0
 */
package com.gov.wiki.business.req.query;

import java.util.List;
import com.gov.wiki.organization.req.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: MaterialExistQuery
 * @Description: 资料匹配查询请求参数
 * @author cys
 * @date 2020年11月25日
 */
@Data
@ApiModel(value = "MaterialExistQuery", description = "资料匹配查询请求参数")
public class MaterialExistQuery extends BaseQuery {

	@ApiModelProperty(value = "排除ID")
	private List<String> excludeIds;
}