package com.gov.wiki.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperUtil {

	public static String objToStr(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		if(o == null) return null;
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
		} catch (JsonProcessingException e) {
			return o.toString();
		}
	}
}
