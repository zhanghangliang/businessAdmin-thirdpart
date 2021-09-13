package com.gov.wiki.message.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.repository.BaseRepository;
import com.gov.wiki.message.entity.McMessageRead;

@Repository
public interface McMessageReadDao extends BaseRepository<McMessageRead, String> {
}