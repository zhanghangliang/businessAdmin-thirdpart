package com.gov.wiki.business.service;

import com.gov.wiki.common.entity.buss.PrivMatterSubject;
import com.gov.wiki.common.service.IBaseService;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrivMatterSubjectService extends IBaseService<PrivMatterSubject, String> {
    List<PrivMatterSubject> findBySubjectId(String subjectId);
    void deleteBySubjectId(String subjectId);
    void deleteBySituationId(String situationId);
    List<PrivMatterSubject> save(String subjectId,List<String> situations);
    List<String> groupUpSituationId(String subjectId);
    List<String> findSituationIdsByUpSituationId(String upsubjectId,String subjectId);
    List<String> findSituationIdsBySubjectId(String subjectId);
}
