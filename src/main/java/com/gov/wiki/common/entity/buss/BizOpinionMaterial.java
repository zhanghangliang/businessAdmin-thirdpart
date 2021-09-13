package com.gov.wiki.common.entity.buss;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.system.SysDictItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
*问题选项材料库
*/
@Setter
@Getter
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@Where(clause = "del_flag != 1")
@Table(name = "biz_opinion_material")
@SQLDelete(sql = "update biz_opinion_material set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_opinion_material set del_flag=1 where id=?")
@ApiModel(value = "BizOpinionMaterial", description = "问题选项材料库")
public class BizOpinionMaterial extends BaseEntity{
	
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*备注
	*/
	@Column(name = "remark", nullable = true)
	@ApiModelProperty(value = "备注")
	private String remark;
	
	/**
	*所属问题选项
	*/
	@ApiModelProperty(value = "所属问题选项")
	@Column(name = "opinion_id", nullable = true)
	private String opinionId;
	
	/**
	*资料ID
	*/
	@Column(name = "material_id", nullable = true)
	@ApiModelProperty(value = "资料ID")
	private String materialId;
	
	/**
	 * 资料
	 */
	@NotFound(action = NotFoundAction.IGNORE)
	@ApiModelProperty(value = "资料")
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "material_id", insertable = false, updatable = false)
	private BizMaterialDepository material;
	
	/**
	*必要性0-非必要 1-必要
	*/
	@Column(name = "necessity", nullable = true)
	@ApiModelProperty(value = "必要性0-非必要 1-必要")
	private Boolean necessity;
	
	/**
	*材料数量
	*/
	@Column(name = "qty", nullable = true)
	@ApiModelProperty(value = "材料数量")
	private Integer qty;
	
	/**
	*验收
	*/
	@Column(name = "check_accept", nullable = true)
	@ApiModelProperty(value = "验收")
	private String checkAccept;
	
	/**
	 * 验收枚举
	 */
	@ApiModelProperty(value = "验收枚举")
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "check_accept", insertable = false, updatable = false)
	private SysDictItem checkAcceptVo;
	
	/**
	*所属问题
	*/
	@Column(name = "question_id", nullable = true)
	@ApiModelProperty(value = "所属问题")
	private String questionId;

	public void addQty(Integer qty) {
		if(qty == null) return;
		if(this.qty == null) this.qty = qty;
		else this.qty+=qty;
	}
}