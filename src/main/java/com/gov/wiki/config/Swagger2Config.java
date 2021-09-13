package com.gov.wiki.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: Swagger2Config
 * @Description: Swagger配置
 * @author cys
 * @date 2019年12月10日
 */
@Slf4j
@Configuration
public class Swagger2Config {
	
	@Bean
	public Docket demoApi() {
		ParameterBuilder ticketPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		ticketPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header")
				.required(true).build(); // header中的ticket参数非必填，传空也可以
		pars.add(ticketPar.build()); // 根据每个方法名也知道当前方法在设置什么参数

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.gov.wiki")).build()
				.globalOperationParameters(pars).apiInfo(apiInfo());
	}

	ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("api swagger document")
				.description("前后端联调swagger api 文档")
				.version("2.1.5.5")
				.build();
	}
}