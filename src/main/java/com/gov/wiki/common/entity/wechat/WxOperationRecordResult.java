/**
 * @Title: WxOperationRecordResult.java
 * @Package com.gov.wiki.common.entity.buss
 * @Description: TODO(用一句话描述该文件做什么)
 * @author cys
 * @date 2020年12月15日
 * @version V1.0
 */
package com.gov.wiki.common.entity.wechat;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.buss.BizMaterialDepository;
import com.gov.wiki.common.entity.system.SysDictItem;
import com.gov.wiki.common.entity.system.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: WxOperationRecordResult
 * @Description: 微信用户操作记录结果详情
 * @author cys
 * @date 2020年12月15日
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "wx_operation_record_result")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update wx_operation_record_result set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update wx_operation_record_result set del_flag=1 where id=?")
@ApiModel(value = "WxOperationRecordResult", description = "微信用户操作记录结果详情")
public class WxOperationRecordResult extends BaseEntity {
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;

	/**
	*所属操作记录ID
	*/
	@Column(name = "record_id", nullable = true)
	@ApiModelProperty(value = "所属操作记录ID")
	private String recordId;
	
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
	*结果附件
	*/
	@Column(name = "result_annex", nullable = true)
	@ApiModelProperty(value = "结果附件")
	private String resultAnnex;
	
	@NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "reference_id", referencedColumnName = "result_annex", insertable = false, updatable = false)
	@ApiModelProperty(value = "结果附件文件列表")
	@OrderBy("sort_no ASC")
    private List<SysFile> resultAnnexFiles;
	
	/**
	*排序号
	*/
	@Column(name = "sort_no", nullable = true)
	@ApiModelProperty(value = "排序号")
	private Integer sortNo;
	
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
	*备注
	*/
	@Column(name = "remark", nullable = true)
	@ApiModelProperty(value = "备注")
	private String remark;
}