package com.drawbluecup.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("项目 API 文档")
                        .version("1.0.0")
                        .description("用户、商品、订单模块的接口文档（Spring Boot 3 兼容版）")
                        .contact(new Contact()
                                .name("你的名字")
                                .email("你的邮箱")
                                .url("项目地址")));
    }
}