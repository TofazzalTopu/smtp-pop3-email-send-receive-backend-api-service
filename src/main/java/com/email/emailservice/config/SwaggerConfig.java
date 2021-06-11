package com.email.emailservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket productApi() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.email.emailservice.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private Contact contact() {
        Contact contact = new Contact(
                "Pennyperfect",
                "http://pennyperfect.com/",
                "info@pennyperfect.com"
        );
        return contact;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pennyperfect Mail Service Api")
                .description("Pennyperfect Mail Service Api")
                .version("1.0")
                .license("Pennyperfect Mail Service")
                .licenseUrl("http://pennyperfect.com/")
                .contact(contact())
                .build();
    }
}
