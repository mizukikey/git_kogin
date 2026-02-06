package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityDisableConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 自前ログインを使う場合の設定
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // CSRF無効（今は簡易版なので）
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/","/customer/login","/manager/login",  "/customer_input", "/css/**", "/js/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .formLogin(login -> login.disable()); // Springの自動ログイン画面を無効化
//
//        return http.build();
//    }
    
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable()) // 簡易版、必要に応じて有効化
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                    "/customer/login", 
//                    "/manager/login",  
//                    "/customer_input",
//                    "/css/**", 
//                    "/js/**",
//                    "/index.html",
//                    "/",
//                    "/customer/customer_top"
//                ).permitAll()
//                .anyRequest().authenticated() // その他はセッション必須
//            )
//            .formLogin(login -> login.disable()); // Spring 自動ログイン画面を無効化
//
//        return http.build();
//    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/customer/login",
                    "/manager/login",
                    "/customer_input",
                    "/css/**",
                    "/js/**",
                    "/index.html",
                    "/",
                    "/**",// ← すべて許可
                    "/customer_input_confirm",
                    "/customer_input_result"
                ).permitAll()
            )
            .formLogin(login -> login.disable());
        return http.build();
    }

    
    
}