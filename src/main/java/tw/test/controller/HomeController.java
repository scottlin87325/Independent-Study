package tw.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;



@Controller

public class HomeController {
	
	/*
    @GetMapping("/login")  // 訪問首頁時顯示 login 頁面
    public String login() {
        return "login";  // 返回 login.html
    }

    @GetMapping("/main")  // 登錄成功後跳轉到的頁面
    public String main() {
        return "main";  // 返回 main.html
    }
/*
    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OidcUser principal, Model model) {
        model.addAttribute("name", principal.getFullName());
        model.addAttribute("email", principal.getEmail());
        return "profile";
    }
    */
    
    
    
    
   
}


