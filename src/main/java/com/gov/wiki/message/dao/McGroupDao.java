package com.gov.wiki.message.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.repository.BaseRepository;
import com.gov.wiki.message.entity.McGroup;

@Repository
public interface McGroupDao extends BaseRepository<McGroup, String> {
}