package tw.test.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tw.test.model.Member;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;

    public OAuth2LoginSuccessHandler(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
            throws IOException, ServletException {
        
        
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email"); //登入者email
        String name = oauth2User.getAttribute("name");	//登入者name

        if (email == null || email.isEmpty()) {
            // 如果 email=null或空
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found");//返回 HTTP 400 錯誤
            return;
        }

        // 檢查用戶是否已經存在
        Member existingMember = memberService.findMemberByAccount(email);
        if (existingMember == null) {
            // 若會員不存在，創建新會員
            Member newMember = new Member();
            newMember.setEmail(email);
            newMember.setMembername(name);
            newMember.setRealname(name); 
            newMember.setPassword(""); 
            memberService.addMember(newMember);
            existingMember = newMember; // 更新 existingMember
        }
        request.getSession().setAttribute("member", existingMember); //存到session
        
        setDefaultTargetUrl("/main"); // 成功處理後轉向
        
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

