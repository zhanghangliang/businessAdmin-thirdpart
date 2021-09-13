package com.gov.wiki.common.entity.wechat;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.BizSubject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "wx_operation_record_count")
@Where(clause = "del_flag != 1")
@ApiModel(value = "WxOperationRecordCount", description = "服务个数统计表")
@DynamicInsert
public class WxOperationRecordCount extends BaseEntity {
    /**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;


	@Column(name = "subject_id", nullable = true)
    private String subjectId;
    
    
    @NotFound(action = NotFoundAction.IGNORE)
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id",insertable = false,updatable = false)
	@ApiModelProperty(value = "关联主题")
    private BizMatterDepositoryMain subject;

    @Column(name = "count", nullable = true)
    private Integer count;

    @Formula("(SELECT t.name FROM biz_subject t WHERE t.id=subject_id)")
    private String subjectName;
}
