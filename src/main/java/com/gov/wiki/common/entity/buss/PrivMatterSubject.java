package com.gov.wiki.common.entity.buss;

import com.gov.wiki.common.entity.BaseEntity;
import com.gov.wiki.common.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "priv_matter_subject")
public class PrivMatterSubject extends IdEntity {

    @Column(name = "subject_id")
    private String subjectId;

    @Column(name = "situation_id")
    private String situationId;

    @Column(name = "up_situation_id")
    private String upSituationId;

    @Formula("(SELECT t.matter_name FROM biz_matter_depository_main t WHERE t.id= up_situation_id)")
    private String fatherName;

    @Formula("(SELECT t.matter_name FROM biz_matter_depository_main t WHERE t.id= subject_id)")
    private String sonName;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "situation_id", referencedColumnName = "id",insertable=false,updatable=false)
    private BizMatterDepositoryMain matterDepositoryMains;
}
