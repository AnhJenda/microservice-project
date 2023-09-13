package com.example.authservice.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
    @author: Dinh Quang Anh
    Date   : 8/4/2023
    Project: spring_school_api
*/
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // Cho phép truy cập từ bất kỳ origin nào
        config.addAllowedHeader("*"); // Cho phép sử dụng bất kỳ header nào
        config.addAllowedMethod("*"); // Cho phép sử dụng bất kỳ phương thức HTTP nào (GET, POST, PUT, DELETE,...)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
