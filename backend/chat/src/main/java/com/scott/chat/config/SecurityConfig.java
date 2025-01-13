package com.scott.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.scott.chat.service.OAuth2LoginSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	 private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

	    public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
	        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
	    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/login", "/login_submit", "/webjars/**", "/css/**", "/js/**").permitAll()  // 允許匿名訪問的頁面
                //.requestMatchers("/main").authenticated() //只有main要驗證
                .anyRequest().permitAll() // 允許所有頁面
                //.anyRequest().authenticated() //所有頁面都要驗證
                
            .and()
            .oauth2Login()
                .loginPage("/login")  // 設定自定義的登入頁面
                .defaultSuccessUrl("/main", true)  // 登錄成功後重定向到 /main 頁面
                .successHandler(oAuth2LoginSuccessHandler) // 自定義成功處理
                .permitAll();
             /*  
            .and()
            .formLogin()  // 表單
                .loginPage("/loginpage")  
                .loginProcessingUrl("/login_submit")  // 提交路徑
                .defaultSuccessUrl("/main", true)  // 成功轉向
                .permitAll();
             */

        
        return http.build();
    }
}