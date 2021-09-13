/**
 * @Title: MemberQuery.java 
 * @Package com.insolu.spm.organization.req 
 * @Description: 人员查询参数实体
 * @author cys 
 * @date 2019年11月8日 下午8:02:08 
 * @version V1.0 
 */
package com.gov.wiki.organization.req.query;

import java.util.List;

import com.gov.wiki.organization.req.BaseQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberQuery", description = "人员参数对象")
public class MemberQuery extends BaseQuery{
	/**
	 * 人员类型
	 */
	@ApiModelProperty(value = "人员类型ID")
	private String memberType;
	
	/**
	 * 人员账号状态
	 */
	@ApiModelProperty(value = "人员账号状态")
	private Integer status;
	
	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private Integer sex;
	
	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "部门ID")
	private String departId;
	
	/**
	 * 人员状态
	 */
	@ApiModelProperty(value = "人员状态")
	private String memberState;
	
	/**
	 * 岗位ID
	 */
	@ApiModelProperty(value = "岗位ID")
	private String postId;
	
	/**
	 * 职务ID
	 */
	@ApiModelProperty(value = "职务ID")
	private String jobId;
	
	/**
	 * 部门下所有人员
	 */
	@ApiModelProperty(value = "是否部门下所有人员，默认否")
	private boolean allPathMember = false;
	
	/**
	 * 分管部门长编码
	 */
	@ApiModelProperty(value = "分管部门长编码")
	private List<String> departRanges;
}