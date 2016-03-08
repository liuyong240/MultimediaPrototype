package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class MySwaggerConfig  {

//    @Bean
//    public Docket sampleDocket(){
//        ApiInfo apiInfo = new ApiInfo(
//                "Swagger-Spring Sample", //My Apps API Title
//                "文档概述blablabla" +
//                        "<br />这个地方可以写很多行<br>可以加html标签", //My Apps API Description
//                "2.0",

//        );
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("sample")
//                .apiInfo(apiInfo)
//                .genericModelSubstitutes(ResponseEntity.class)
//                .select()
//                .paths(regex("/swagger/api/.*")) // and by paths
//                .build();
//
//    }

    @Bean
    public Docket apiDocket(){
        ApiInfo apiInfo = new ApiInfo(
                "API",
                "xxxxxx",
                "1.0",
                "http://myapp/terms/",
                "dongxu",
                "",
                ""
        );
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("apis")
                .apiInfo(apiInfo)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex("/api/.*"))
                .build();

    }

    @Bean
    public Docket adminDocket(){
        ApiInfo apiInfo = new ApiInfo(
                "多媒体Admin系统API",
                "后台管理",
                "1.0",
                "http://myapp/terms/",
                "dongxu",
                "",
                ""
        );
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .apiInfo(apiInfo)
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex("/admin/.*"))
                .build();

    }



}


