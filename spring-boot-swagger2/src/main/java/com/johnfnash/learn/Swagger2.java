package com.johnfnash.learn;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //���� swagger2 ֧��
@ConditionalOnExpression("${swagger.enable}") //�Ƿ���swagger
public class Swagger2 {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
													  .select()
													  .apis(RequestHandlerSelectors.basePackage("com.johnfnash.learn"))
													  .paths(PathSelectors.any())
													  .build();
				
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("springboot����swagger����api�ĵ�")
								   .description("�����ŵ�restfun���")
								   .termsOfServiceUrl("http://localhost:8080/")
								   .version("1.0")
								   .build();
				
	}
	
}
