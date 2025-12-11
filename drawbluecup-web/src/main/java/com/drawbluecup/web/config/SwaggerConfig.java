package com.drawbluecup.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration // 标记为配置类，Spring启动时自动加载

public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // 1. 配置作者信息（对应的“名字 - Website”，“Send email to 名字”）
        Contact contact = new Contact()
                .name("mist-and-clouds")
                .url("https://github.com/TTMG4RR/MyRepository")
                .email("2186424183@qq.com");

        // 2. 配置文档基本信息（标题、描述、版本等）
        Info info = new Info()
                .title("绘蓝杯后端项目 API 文档") // 文档标题（对应页面上的“项目 API 文档”）
                .description("用户、商品、订单模块的接口文档（Spring Boot 3 兼容版）") // 文档描述
                .version("1.0.1") // 文档版本
                .contact(contact); // 关联上面配置的作者信息

        // 3. 核心：配置全局Token请求头
        // 3.1 定义Token的参数规则（放在请求头，字段名和你的拦截器保持一致）
        Components components = new Components()
                .addSecuritySchemes("accessToken", // 自定义一个标识名
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY) // Token类型：API密钥
                                .in(SecurityScheme.In.HEADER) // Token放在请求头里
                                .name("Access-Token") // 请求头的字段名（必须和你的Token拦截器里的字段名一致！）
                                //name(***)与TokenInterceptor里的request.getHeader("***")一致
                                .description("登录后获取的用户Token")); // 描述，方便演示时理解

        // 3.2 全局启用Token校验（所有接口都会带这个请求头）
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("accessToken");

        // 4. 组装并返回OpenAPI对象
        return new OpenAPI()
                .info(info) // 原有文档信息
                .components(components) // 关联Token配置
                .security(List.of(securityRequirement)); // 全局启用Token
    }

}
