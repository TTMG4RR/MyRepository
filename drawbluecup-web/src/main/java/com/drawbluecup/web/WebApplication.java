
package com.drawbluecup.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.drawbluecup.mapper") // 扫描所有 Mapper 接口
@ComponentScan("com.drawbluecup")    // 扫描所有业务模块的组件（Service、Controller 等）
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}