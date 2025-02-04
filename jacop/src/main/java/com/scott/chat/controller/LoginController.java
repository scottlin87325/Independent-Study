package com.scott.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.scott.chat.model.Member;
import com.scott.chat.service.MemberService;
import com.scott.chat.model.CustomUserDetails;
import com.scott.chat.util.BCrypt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
// 登入相關控制器
@Controller
@SessionAttributes("member")  // 將member對象存儲在session中
public class LoginController {
    
    @Autowired
    private MemberService memberService;

    // 顯示註冊頁面
    @RequestMapping("/registerpage")
    public String reg(Model model) {
        Member member = new Member();
        model.addAttribute("member", member);
        return "registerpage";
    }
   
    // 處理註冊表單提交
    @PostMapping("/reg_submit")
    public String regSubmit(@ModelAttribute Member member, 
            BindingResult result, Model model) {
       // 檢查帳號是否已存在
        Member existingMember = memberService.findMemberByAccount(member.getEmail());
       
        if (existingMember != null) {
            model.addAttribute("error", "帳號已經存在，請選擇其他帳號");
            return "registerpage";
        }
       
        memberService.addMember(member);
        return "redirect:/loginpage";
    }
   
    // 顯示登入頁面
    @GetMapping("/loginpage")
    public String login(Model model, HttpServletRequest request, HttpSession session) {
        // 檢查用戶是否已登入
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() 
                && !authentication.getPrincipal().equals("anonymousUser")) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Member member = userDetails.getMember();
            session.setAttribute("member", member);
            return "redirect:/main";
        }

        // 處理錯誤訊息
        Object error = request.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            System.out.println("Adding error to model: " + error);
        }

        // 清除舊的session
        invalidateExistingSessions(request);

        if (!model.containsAttribute("member")) {
            model.addAttribute("member", new Member());
        }

        return "loginpage";
    }

    // 清除現有session的輔助方法
    private void invalidateExistingSessions(HttpServletRequest request) {
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            existingSession.invalidate();
        }
    }

    // 處理登入頁面GET請求
    @PostMapping("/loginpage")
    public String loginpage(@ModelAttribute Member member) {
        return "loginpage";
    }
   
    // 顯示主頁面
    @GetMapping("/main")
    public String mainPage(HttpSession session, HttpServletResponse response, Model model) {
        // 檢查session是否存在
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/loginpage";
        }
        
        // 更新用戶信息
        Member updatedMember = memberService.findMemberByAccount(member.getEmail());
        if (updatedMember != null) {
            session.setAttribute("member", updatedMember);
            model.addAttribute("member", updatedMember);
            
            // 設置session cookie
            String sessionId = UUID.randomUUID().toString();
            Cookie sessionCookie = new Cookie("SESSION", sessionId);
            sessionCookie.setMaxAge(3600); // 1小時
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
        }
        
        System.out.println("當前用戶: " + member.getMembername());
        return "home";
    }
   
    // 顯示忘記密碼頁面
    @GetMapping("/forgotPassword")
    public String showForgotPasswordPage() {
        return "forgotPassword";
    }

    // 顯示聊天頁面
    @GetMapping("/chat")
    public String showchat() {
        return "chat";
    }

    // 處理登出請求
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // 清除session
        session.invalidate();
        
        // 清除所有相關cookie
        Cookie[] cookies = new Cookie[] {
            new Cookie("SESSION", null),
            new Cookie("remember-me", null)
        };
        
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        
        return "redirect:/loginpage";
    }

    // 處理session過期情況
    @GetMapping("/session-expired")
    public String handleSessionExpired(Model model) {
        model.addAttribute("error", "登入已過期，請重新登入");
        return "redirect:/loginpage";
    }

    // 為所有請求添加通用屬性
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("currentUser", member);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
    }

    // 創建新的session
    protected void createNewSession(HttpSession session, Member member, HttpServletResponse response) {
        // 設置session屬性
        session.setAttribute("member", member);
        session.setAttribute("currentUserId", member.getMemberid());
        session.setAttribute("currentUserName", member.getMembername());
        
        // 設置session超時時間
        session.setMaxInactiveInterval(3600); // 1小時
        
        // 創建新的session cookie
        String sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("SESSION", sessionId);
        sessionCookie.setMaxAge(3600);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    // 清除session
    protected void clearSession(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie sessionCookie = new Cookie("SESSION", null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }
}