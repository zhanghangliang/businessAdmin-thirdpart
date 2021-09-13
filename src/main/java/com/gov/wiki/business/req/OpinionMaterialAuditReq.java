/**
 * @Title: OpinionMaterialAuditReq.java
 * @Package com.gov.wiki.business.req
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年8月25日
 * @version V1.0
 */
package com.gov.wiki.business.req;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.buss.BizOpinionMaterialAudit;
import com.gov.wiki.common.utils.JSONUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: OpinionMaterialAuditReq
 * @Description: 问题选项材料审批请求对象
 * @author cys
 * @date 2020年8月25日
 */
@Data
@ApiModel(value = "OpinionMaterialAuditReq", description = "问题选项材料审批请求对象")
public class OpinionMaterialAuditReq {
	
	/**
	 * 唯一标识
	 */
	@ApiModelProperty(value = "唯一标识")
	private String id;
	
	/**
	*备注
	*/
	@ApiModelProperty(value = "备注")
	private String remark;
	
	/**
	*所属问题选项审核记录
	*/
	@ApiModelProperty(value = "所属问题选项审核记录")
	private String opinionAuditId;
	
	/**
	*资料ID
	*/
	@Check(title = "资料", nullable = true)
	@ApiModelProperty(value = "资料ID")
	private String materialId;
	
	/**
	*必要性0-非必要 1-必要
	*/
	@ApiModelProperty(value = "必要性0-非必要 1-必要")
	private Boolean necessity;
	
	/**
	*材料数量
	*/
	@ApiModelProperty(value = "材料数量")
	private Integer qty;
	
	/**
	*验收
	*/
	@ApiModelProperty(value = "验收")
	private String checkAccept;
	
	/**
	*所属问题审核记录
	*/
	@ApiModelProperty(value = "所属问题审核记录")
	private String questionAuditId;
	
	public BizOpinionMaterialAudit toEntity() {
		BizOpinionMaterialAudit m = new BizOpinionMaterialAudit();
		m.setId(JSONUtil.strToNull(this.id));
		m.setCheckAccept(JSONUtil.strToNull(this.checkAccept));
		m.setDelFlag(false);
		m.setMaterialId(JSONUtil.strToNull(this.materialId));
		m.setNecessity(this.necessity);
		m.setOpinionAuditId(JSONUtil.strToNull(this.opinionAuditId));
		m.setQty(this.qty);
		m.setQuestionAuditId(JSONUtil.strToNull(this.questionAuditId));
		m.setRemark(JSONUtil.strToNull(this.remark));
		return m;
	}
}