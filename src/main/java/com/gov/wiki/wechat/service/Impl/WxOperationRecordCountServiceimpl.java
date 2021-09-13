package com.gov.wiki.wechat.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.PredicateBuilder;
import com.github.wenhao.jpa.Specifications;
import com.gov.wiki.common.entity.wechat.WxOperationRecordCount;
import com.gov.wiki.common.service.BaseServiceImpl;
import com.gov.wiki.wechat.dao.WxOperationRecordCountDao;
import com.gov.wiki.wechat.service.WxOperationRecordCountService;

@Service
public class WxOperationRecordCountServiceimpl extends BaseServiceImpl<WxOperationRecordCount, String, WxOperationRecordCountDao> implements WxOperationRecordCountService {
    @Override
    public WxOperationRecordCount findBySubjectId(String subjectid) {
        return this.baseRepository.findBySubjectId(subjectid);
    }

	@Override
	public Page<WxOperationRecordCount> findEffectiveAll(Pageable pageable) {
		PredicateBuilder<WxOperationRecordCount> builder = Specifications.and();
		//builder.ne("subject.matterName", null,"");
		return this.baseRepository.findAll(builder.build(), pageable);
	}

	@Override
	public void addCountBySubjectId(int i, String subjectId) {
		WxOperationRecordCount recordCount = this.findBySubjectId(subjectId);
        if(recordCount==null){
            recordCount=new WxOperationRecordCount();
            recordCount.setCount(1);
            recordCount.setSubjectId(subjectId);
        }else{
            recordCount.setCount(recordCount.getCount()+1);
        }
        this.saveOrUpdate(recordCount);
	}
}
