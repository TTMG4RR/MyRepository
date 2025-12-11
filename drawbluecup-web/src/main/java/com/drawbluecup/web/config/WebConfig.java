package com.drawbluecup.web.config;


import com.drawbluecup.JWT.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类（注册拦截器）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Token拦截器
        registry.addInterceptor(new TokenInterceptor())
                .addPathPatterns("/**")//拦截所有请求
        // 白名单：Swagger路径+跨域测试页面路径+登录接口
                .excludePathPatterns(
                "/swagger-ui/**",  // Swagger页面
                "/v3/api-docs/**",  // Swagger接口文档
                "/http://127.0.0.1:5500/**", // 比如你的跨域测试页是/cors-test，就写/cors-test/**
                "/user/login"       // 登录接口（获取Token的入口必须放行）
        );
    }
}