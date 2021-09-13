package com.gov.wiki.business.service.impl;

import com.gov.wiki.business.dao.PrivMatterSubjectDao;
import com.gov.wiki.business.service.BizMatterDepositoryMainService;
import com.gov.wiki.business.service.PrivMatterSubjectService;
import com.gov.wiki.common.entity.buss.BizMatterDepositoryMain;
import com.gov.wiki.common.entity.buss.PrivMatterSubject;
import com.gov.wiki.common.service.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivMatterSubjectServiceimpl extends BaseServiceImpl<PrivMatterSubject,String, PrivMatterSubjectDao> implements PrivMatterSubjectService {
    @Autowired
    private BizMatterDepositoryMainService matterDepositoryMainService;

    @Override
    public List<PrivMatterSubject> findBySubjectId(String subjectId) {
        return this.baseRepository.findBySubjectId(subjectId);
    }

    @Override
    public void deleteBySubjectId(String subjectId) {
        this.baseRepository.deleteBySubjectId(subjectId);
    }

    @Override
    public List<PrivMatterSubject> save(String subjectId, List<String> situations) {
        List<PrivMatterSubject> privMatterSubjects=new ArrayList<>();
        for (String situation : situations) {
            PrivMatterSubject privMatterSubject = new PrivMatterSubject();
            BizMatterDepositoryMain byId = matterDepositoryMainService.findById(situation);
            privMatterSubject.setSubjectId(subjectId);
            privMatterSubject.setUpSituationId(byId.getUpmatterId());
            privMatterSubject.setSituationId(situation);
            privMatterSubjects.add(privMatterSubject);
        }
        return this.baseRepository.saveAll(privMatterSubjects);
    }

    @Override
    public List<String> groupUpSituationId(String subjectId) {
        return this.baseRepository.groupUpSituationId(subjectId);
    }

    @Override
    public List<String> findSituationIdsByUpSituationId(String upsubjectId,String subjectId) {
        return this.baseRepository.findSituationIdsByUpSituationId(upsubjectId,subjectId);
    }

    @Override
    public List<String> findSituationIdsBySubjectId(String subjectId) {
        return this.baseRepository.findSituationIdsBySubjectId(subjectId);
    }

    @Override
    public void deleteBySituationId(String situationId) {
        this.baseRepository.deleteBySituationId(situationId);
    }
}
