package com.scott.chat.security;

import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import com.scott.chat.model.CustomUserDetails;
import com.scott.chat.model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private static final Logger logger = Logger.getLogger(CustomAuthenticationSuccessHandler.class.getName());
    
    private final SecurityContextRepository securityContextRepository;
    
    public CustomAuthenticationSuccessHandler(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member member = userDetails.getMember();
            
            // 創建新的 SecurityContext
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            
            // 保存 SecurityContext 到 repository
            securityContextRepository.saveContext(context, request, response);
            
            // 設置 session
            HttpSession session = request.getSession(true);
            session.setAttribute("member", member);
            session.setAttribute("currentUserId", member.getMemberid());
            session.setAttribute("currentUserName", member.getMembername());
            
            // 設置 session 超時時間 (30分鐘)
            session.setMaxInactiveInterval(1800);
            
            logger.fine("Authentication success for user: " + member.getMembername());
            
            // 重定向到主頁
            response.sendRedirect("/main");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in authentication success handler", e);
            throw e;
        }
    }
}