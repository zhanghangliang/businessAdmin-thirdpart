package com.gov.wiki.common.entity.buss;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gov.wiki.common.check.Check;
import com.gov.wiki.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@Table(name = "biz_study_record")
public class BizStudyRecord extends BaseEntity {
    @ApiModelProperty(name = "memberId", value = "学习人员编号")
    @Column(name = "member_id")
    @Check
    private String memberId;

    @ApiModelProperty(name = "studyId", value = "学习资料编号")
    @Column(name = "study_id")
    @Check
    private String studyId;

    @ApiModelProperty(name = "studyTime", value = "学习时间")
    @Column(name = "study_time")
    private String studyTime;

    @ApiModelProperty(name = "memberName", value = "学习人员姓名")
    @Formula("(SELECT t.real_name FROM org_member t where t.id=member_id)")
    private String memberName;
    
    @NotFound(action = NotFoundAction.IGNORE)
	@OneToOne(cascade = CascadeType.REFRESH, optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "study_id", insertable = false, updatable = false)
    private BizStudy study;
}
