package com.gov.wiki.common.entity.buss;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.utils.BeanUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
*题库表
*/
@Setter
@Getter
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "biz_question")
@Where(clause = "del_flag != 1")
@SQLDelete(sql = "update biz_question set del_flag=1 where id=?")
@SQLDeleteAll(sql = "update biz_question set del_flag=1 where id=?")
@ApiModel(value = "BizQuestion", description = "题库表")
public class BizQuestion extends BaseEntity{
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	*题目名称
	*/
	@Column(name = "name", nullable = true)
	@ApiModelProperty(value = "题目名称")
	private String name;
	
	/**
	*描述
	*/
	@Column(name = "description", nullable = true)
	@ApiModelProperty(value = "描述")
	private String description;
	
	/**
	*选择策略 1-串行 2-并行
	*/
	@Column(name = "strategy", nullable = true)
	@ApiModelProperty(value = "选择策略 1-串行 2-并行")
	private Integer strategy;
	
	@Column(name = "remark", nullable = true)
	@ApiModelProperty(value = "备注")	
	private String remark;
	
	/**
	 * 问题选项列表
	 */
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "question_id",insertable =false ,updatable = false)
	@ApiModelProperty(value = "问题选项列表")
	@OrderBy("sort_no ASC")
	@NotFound(action = NotFoundAction.IGNORE)
	@Fetch(FetchMode.SELECT)
	private List<BizQuestionOpinion> opinionList;
	
	/**
	 * 修改次数
	 */
	@Column(name = "modify_times", nullable = true)
	@ApiModelProperty(value = "修改次数")	
	private Integer modifyTimes;
	
	/**
	 * 版本号
	 */
	@Column(name = "version", nullable = true)
	@ApiModelProperty(value = "版本号")	
	private String version;
	
	/**
	 * 回收标志
	 */
	@Column(name = "recycling_mark", nullable = true)
	@ApiModelProperty(value = "回收标志")	
	private Boolean recyclingMark;
	
	public void toChangeOpinions(List<BizQuestionOpinionAudit> list) {
		opinionList = new ArrayList<BizQuestionOpinion>();
		if(list == null) return;
		for (BizQuestionOpinionAudit q : list) {
			BizQuestionOpinion o = BeanUtils.copyProperties(q, BizQuestionOpinion.class);
			o.setId(q.getOpinionId());
			opinionList.add(o);
		}
	}


	public BizQuestionAudit toAudit() {
		BizQuestionAudit audit = BeanUtils.copyProperties(this, BizQuestionAudit.class);
		audit.setReleaseId(this.getId());
		audit.setOpinionList(toOPinions());
		return audit;
	}


	private List<BizQuestionOpinionAudit> toOPinions() {
		List<BizQuestionOpinionAudit> list = new ArrayList<BizQuestionOpinionAudit>();
		if(this.opinionList == null || this.opinionList.isEmpty()) return list;
		for (BizQuestionOpinion o : opinionList) {
			list.add(o.toAudit());
		}
		return list;
	}
}