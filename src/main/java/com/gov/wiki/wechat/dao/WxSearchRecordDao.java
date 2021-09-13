package com.gov.wiki.wechat.dao;

import com.gov.wiki.common.entity.wechat.WxSearchRecord;
import com.gov.wiki.common.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WxSearchRecordDao extends BaseRepository<WxSearchRecord, String> {
    List<WxSearchRecord> findByMemberIdAndSearchType(String memberId,Integer searchtype);
    void deleteByMemberIdAndSearchType(String memberId,Integer searchtype);
    WxSearchRecord findBySearchContentAndSearchType(String SearchContent,Integer searchtype);
}
