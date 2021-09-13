package com.gov.wiki.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "gov.wiki.file")
public class FileConfig {

	/**
	 * 文件保存路径
	 */
	private String savePath;
	
	/**
	 * 文件显示前缀
	 */
	private String showPrefix;
	
	/**
	 * 缩放比例
	 */
	private Float scale;
	
	private int width;

}