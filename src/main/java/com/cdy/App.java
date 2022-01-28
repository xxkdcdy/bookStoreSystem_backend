package com.cdy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// spring boot 入口类

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // 项目启动，自动寻找子包下controller
        SpringApplication.run(App.class, args);
    }
}
