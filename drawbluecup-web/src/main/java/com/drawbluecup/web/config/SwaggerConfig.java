package com.drawbluecup.web.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        // 3. 返回OpenAPI对象，Swagger会自动加载这些配置
        return new OpenAPI().info(info);
    }

}
