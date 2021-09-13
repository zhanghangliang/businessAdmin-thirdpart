package com.gov.wiki.wechat.res;

import java.util.List;

import com.gov.wiki.common.entity.buss.BizSubjectQaRelationship;

import lombok.Data;

@Data
public class NextQuestionRes {

	private String subjectId;
	private List<BizSubjectQaRelationship> ships;
}
