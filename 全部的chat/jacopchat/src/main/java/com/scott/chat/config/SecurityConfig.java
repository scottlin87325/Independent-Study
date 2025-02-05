package com.scott.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.scott.chat.security.CustomAuthenticationFailureHandler;
import com.scott.chat.security.CustomAuthenticationSuccessHandler;
import com.scott.chat.security.OAuth2LoginSuccessHandler;
import com.scott.chat.service.CustomUserDetailsService;

// 安全配置類，用於設置Spring Security的各項安全機制
//Session 是指使用者與網站/應用程式之間的一段互動時期，主要用途包括
//記錄使用者登入狀態、存儲暫時性資料、追蹤使用者行為
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
    @Autowired
    private CustomUserDetailsService userDetailsService;  // 自定義用戶詳情服務
  
    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;  // 自定義認證失敗處理器
  
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;  // OAuth2登入成功處理器

    public SecurityConfig(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    // 密碼加密器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
  
    // 配置認證提供者
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 配置認證管理器
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 配置安全上下文儲存庫
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // 配置認證成功處理器
    @Bean
    public CustomAuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(securityContextRepository());
    }

    // 配置CORS（跨域資源共享）
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");  // 允許的源
        configuration.addAllowedMethod("*");  // 允許的HTTP方法
        configuration.addAllowedHeader("*");  // 允許的HTTP頭
        configuration.setAllowCredentials(true);  // 允許攜帶認證信息
      
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 配置安全過濾鏈
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 禁用CSRF保護
            .csrf(csrf -> csrf.disable())
            // 配置請求授權
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(  // 允許訪問的公開路徑
                    "/login", 
                    "/login_submit", 
                    "/loginpage",
                    "/registerpage",
                    "/reg_submit",
                    "/forgotPassword",
                    "/reset-password/**",
                    "/api/token/**",
                    "/webjars/**", 
                    "/css/**", 
                    "/js/**",
                    "/images/**",
                    "/static/**",
                    "/uploads/**",
                    "/home",
                    "/home/**"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()  // 允許GET訪問貼文API
                .anyRequest().authenticated()  // 其他請求需要認證
            )
            // 配置表單登入
            .formLogin(form -> form
                .loginPage("/loginpage")  // 登入頁面
                .usernameParameter("email")  // 用戶名參數名
                .passwordParameter("password")  // 密碼參數名
                .loginProcessingUrl("/login")  // 登入處理URL
                .successHandler(authenticationSuccessHandler())  // 登入成功處理器
                .failureHandler(authenticationFailureHandler)  // 登入失敗處理器
                .permitAll()
            )
            // 配置OAuth2登入
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/loginpage")
                .defaultSuccessUrl("/main", true)
                .successHandler(oAuth2LoginSuccessHandler)
                .permitAll()
            )
            // 配置登出
            .logout(logout -> logout
                .logoutUrl("/logout")  // 登出URL
                .logoutSuccessUrl("/loginpage")  // 登出成功後的跳轉頁面
                .deleteCookies("JSESSIONID")  // 刪除Cookie
                .invalidateHttpSession(true)  // 使session失效
                .clearAuthentication(true)  // 清除認證信息
                .permitAll()
            )
            // 配置session管理
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // session創建策略
                .maximumSessions(5)  // 最大session數
                .maxSessionsPreventsLogin(false)  // 超過最大session數時的處理策略
                .expiredUrl("/loginpage?expired")  // session過期跳轉頁面
                .and()
                .sessionFixation().newSession()  // session固定攻擊防護
                .invalidSessionUrl("/loginpage?invalid")  // 無效session跳轉頁面
            )
            // 配置安全上下文
            .securityContext(context -> context
                .securityContextRepository(securityContextRepository())
                .requireExplicitSave(true)
            );
            
        return http.build();
    }
}