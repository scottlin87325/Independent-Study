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
        
        // 確保 Authentication 類型正確
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        if (email == null || email.isEmpty()) {
            // 如果 email 不存在，處理錯誤
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email is missing from OAuth2 provider.");
            return;
        }

        // 檢查用戶是否已經存在
        Member existingMember = memberService.findMemberByAccount(email);
        if (existingMember == null) {
            // 若會員不存在，創建新會員
            Member newMember = new Member();
            newMember.setAccount(email);
            newMember.setName(name);
            newMember.setRealname(name); // 使用第三方登入提供的姓名
            newMember.setPasswd(""); // 第三方登入無密碼
            memberService.addMember(newMember);
            existingMember = newMember;
        }
        request.getSession().setAttribute("member", existingMember); 
        
        setDefaultTargetUrl("/main");
        // 成功處理後的重定向
        super.onAuthenticationSuccess(request, response, authentication);
    }
}

