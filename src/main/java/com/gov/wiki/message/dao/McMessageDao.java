package com.gov.wiki.message.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.repository.BaseRepository;
import com.gov.wiki.message.entity.McMessage;

@Repository
public interface McMessageDao extends BaseRepository<McMessage, String> {
}