package com.gov.wiki.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.gov.wiki.common.configuration.AuditingEntityListener;

@Configuration
public class UserAuditor {

	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	public DruidDataSource druidDataSource(){
	      return new DruidDataSource();
	}
	
	@Bean
	public AuditingEntityListener duditingEntityListener() {
		return new AuditingEntityListener();
	}
}
