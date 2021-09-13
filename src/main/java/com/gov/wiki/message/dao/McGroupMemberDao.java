package com.gov.wiki.message.dao;

import org.springframework.stereotype.Repository;

import com.gov.wiki.common.repository.BaseRepository;
import com.gov.wiki.message.entity.McGroupMember;

@Repository
public interface McGroupMemberDao extends BaseRepository<McGroupMember, String> {
}