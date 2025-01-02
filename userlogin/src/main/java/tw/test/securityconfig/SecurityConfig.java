package tw.test.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import tw.test.service.OAuth2LoginSuccessHandler;


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
                .requestMatchers("/", "/login", "/login**", "/webjars/**").permitAll()  // 允許匿名訪問的頁面
                .anyRequest().permitAll() // 允許所有頁面匿名訪問
            .and()
            .oauth2Login()
                .loginPage("/login")  // 設定自定義的登入頁面
                .defaultSuccessUrl("/main", true)  // 登錄成功後重定向到 /main 頁面
                .successHandler(oAuth2LoginSuccessHandler) // 自定義成功處理
                .permitAll();
        return http.build();
    }
    
  
}














