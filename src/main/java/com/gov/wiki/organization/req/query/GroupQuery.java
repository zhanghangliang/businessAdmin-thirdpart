/**
 * @Title: GroupQuery.java 
 * @Package com.insolu.spm.organization.req.query 
 * @Description: 组查询参数对象
 * @author cys 
 * @date 2019年11月11日 下午4:24:35 
 * @version V1.0 
 */
package com.gov.wiki.organization.req.query;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "GroupQuery", description = "组查询参数对象")
public class GroupQuery extends BaseQuery{

}
