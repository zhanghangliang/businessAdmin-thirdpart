package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizSubjectQaRelationshipAudit;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizSubjectQaRelationshipAuditDao
 * @Description: 主题问答关系管理审批记录管理DAO层级
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizSubjectQaRelationshipAuditDao extends BaseRepository<BizSubjectQaRelationshipAudit, String> {
}