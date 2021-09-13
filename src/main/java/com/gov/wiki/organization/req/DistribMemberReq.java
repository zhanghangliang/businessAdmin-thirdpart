/**
 * @Title: DistribMemberReq.java 
 * @Package com.insolu.spm.organization.req 
 * @Description: 分配人员请求参数
 * @author cys 
 * @date 2019年11月8日 下午9:04:21 
 * @version V1.0 
 */
package com.gov.wiki.organization.req;

import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DistribMemberReq", description = "分配人员参数对象")
public class DistribMemberReq {

	@ApiModelProperty(value = "角色ID")
	private String roleId;
	
	@ApiModelProperty(value = "人员ID")
	List<String> memberIds;
}