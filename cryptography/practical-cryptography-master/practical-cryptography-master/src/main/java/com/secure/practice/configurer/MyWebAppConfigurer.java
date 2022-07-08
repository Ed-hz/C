package com.secure.practice.configurer;

import com.secure.practice.interceptor.AdminLoginInterceptor;
import com.secure.practice.interceptor.UserLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor adminLoginInterceptor = new AdminLoginInterceptor(stringRedisTemplate);
        UserLoginInterceptor userLoginInterceptor = new UserLoginInterceptor(stringRedisTemplate);
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/user/**").excludePathPatterns("/user/reg").excludePathPatterns("/user/login").excludePathPatterns("/user/getVerifyCodePic");
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/ca/**").excludePathPatterns("/ca/caRecalled").excludePathPatterns("/ca/downloadCaByID");
        registry.addInterceptor(adminLoginInterceptor).addPathPatterns("/su/**").excludePathPatterns("/su/login");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+System.getProperty("user.dir")+"/img/");
//        registry.addResourceHandler("/CA/**").addResourceLocations("file:"+System.getProperty("user.dir")+"/CA/");
        registry.addResourceHandler("/root.cer").addResourceLocations("file:"+System.getProperty("user.dir")+"/CA/root.cer");
        registry.addResourceHandler("/personal.cer").addResourceLocations("file:"+System.getProperty("user.dir")+"/CA/personal.cer");
        registry.addResourceHandler("/commercial.cer").addResourceLocations("file:"+System.getProperty("user.dir")+"/CA/commercial.cer");
    }

}

