package com.gov.wiki.business.dao;

import com.gov.wiki.common.entity.buss.PrivMatterSubject;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PrivMatterSubjectDao extends BaseRepository<PrivMatterSubject,String> {
    List<PrivMatterSubject> findBySubjectId(String subjectId);

    @Query("SELECT t.upSituationId  FROM PrivMatterSubject t where t.subjectId=?1 group by t.upSituationId")
    List<String> groupUpSituationId(String subjectId);

    @Query("SELECT t.situationId  FROM PrivMatterSubject t where t.upSituationId=?1 and t.subjectId=?2")
    List<String> findSituationIdsByUpSituationId(String upsubjectId,String subjectId);

    @Query("SELECT t.situationId  FROM PrivMatterSubject t where t.subjectId=?1")
    List<String> findSituationIdsBySubjectId(String subjectId);

    @Modifying
    @Transactional
    @Query("delete from PrivMatterSubject t where t.subjectId=?1")
    void deleteBySubjectId(String subjectId);

    @Modifying
    @Transactional
    @Query("delete from PrivMatterSubject t where t.situationId=?1")
    void deleteBySituationId(String situationId);
}
