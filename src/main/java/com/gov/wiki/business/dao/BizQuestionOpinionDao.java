package com.gov.wiki.business.dao;

import org.springframework.stereotype.Repository;
import com.gov.wiki.common.entity.buss.BizQuestionOpinion;
import com.gov.wiki.common.repository.BaseRepository;

/**
 * @ClassName: BizQuestionOpinionDao
 * @Description: 问题选项管理DAO层接口
 * @author cys
 * @date 2020年8月25日
 */
@Repository
public interface BizQuestionOpinionDao extends BaseRepository<BizQuestionOpinion, String> {
}