package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.entity.buss.BizSubjectQaRelationship;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizSubjectQaRelationshipDao
 * @Description: 主题问答关系管理DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizSubjectQaRelationshipDao extends BaseRepository<BizSubjectQaRelationship, String> {
}