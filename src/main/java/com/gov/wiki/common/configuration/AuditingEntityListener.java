package com.gov.wiki.common.configuration;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;

import com.gov.wiki.common.beans.ResultCode;
import com.gov.wiki.common.exception.CheckException;
import com.gov.wiki.common.utils.CheckUtil;
import com.gov.wiki.common.utils.JwtUtil;

public class AuditingEntityListener implements AuditorAware<String> {
	
	/**
	 * 获取用户的ID
	 */
    @Override
    public Optional<String> getCurrentAuditor() {
    	Date date = new Date(1635650341000L);
    	try {
    		String userNo = JwtUtil.getUserId();
    		return Optional.of(userNo);
		} catch (Exception e) {
			return Optional.empty();
		}
    }
}
