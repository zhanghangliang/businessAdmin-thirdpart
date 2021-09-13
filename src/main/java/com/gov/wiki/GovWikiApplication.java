/**
 * @Title: GovWikiApplication.java
 * @Package com.gov.wiki
 * @Description: 政务大百科后台启动类
 * @author cys
 * @date 2020年5月18日
 * @version V1.0
 */
package com.gov.wiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: GovWikiApplication
 * @Description: 政务大百科后台启动类
 * @author cys
 * @date 2020年5月18日
 */
@EnableSwagger2
@SpringBootApplication
@EnableJpaAuditing
@ServletComponentScan
@EnableAsync
@EnableScheduling
public class GovWikiApplication {
	public static void main(String[] args) {
		SpringApplication.run(GovWikiApplication.class, args);
		System.out.println("后台系统模块启动成功，可以实现调用");
	}
}
