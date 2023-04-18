package com.example.ShopeeClone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;import org.springframework.web.cors.CorsConfiguration;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {




    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors().and().csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**","/uploads/**","/api/v1/FileUpload/files/**","/api/v1/category","/api/v1/Products")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/uploads/**", "/images/**");
    }
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // アクセス許可するURL
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        config.setAllowedHeaders(Arrays.asList("*"));
//
//        config.setAllowCredentials(true);
//        config.addExposedHeader("*");
//
//        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
//        configSource.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(configSource);
//    }

}