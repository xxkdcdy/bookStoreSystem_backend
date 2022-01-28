package com.cdy.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadMapping implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将upfile请求下的所有文件保存到物理磁盘中
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:d:/j2ee/bookStoreSystem/img/");
    }
}
