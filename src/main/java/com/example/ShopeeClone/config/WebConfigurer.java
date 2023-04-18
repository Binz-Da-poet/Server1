package com.example.ShopeeClone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@EnableWebMvc
public class WebConfigurer implements WebMvcConfigurer {
    public static String uploadDirectory = Paths.get("uploads").toString();

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //tất cà những request có đường dẫn /uploads/**  (addResourceHandler)
        //sẽ được tìm trong thư mục uploads (addResourceLocations).
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + uploadDirectory+"\\");
    }
}
