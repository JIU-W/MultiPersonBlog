package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    //加自定义拦截器JwtInterceptor，设置拦截规则：除了登录,注册,和文件相关的接口，其他接口都需要拦截
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/files/**");
    }

}
