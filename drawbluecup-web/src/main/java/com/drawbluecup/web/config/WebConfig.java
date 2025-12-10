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
                .addPathPatterns("/**");//拦截所有请求
    }
}