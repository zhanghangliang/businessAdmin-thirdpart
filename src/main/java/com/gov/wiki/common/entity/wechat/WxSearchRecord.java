package com.gov.wiki.common.entity.wechat;

import com.gov.wiki.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Wx_search_record")
@Where(clause = "del_flag != 1")
@ApiModel(value = "WxSearchRecord", description = "用户操作记录表")
@DynamicInsert
public class WxSearchRecord extends BaseEntity {
    @Column(name = "member_id", nullable = true)
    private String memberId;

    @Column(name = "search_content", nullable = true)
    private String searchContent;

    @Column(name = "count", nullable = true)
    private Integer count;

    @Column(name = "search_type", nullable = true)
    private Integer searchType;
}
