package tw.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tw.test.model.Member;
import tw.test.service.MemberService;
import tw.test.util.BCrypt;



@Controller
@SessionAttributes("member") //多次請求
public class LoginController {
	@Autowired
	private MemberService memberService;

	
	//註冊
	@RequestMapping("/registerpage")
	public String reg(Model model) {
		Member member = new Member();
		model.addAttribute("member", member);
		return "registerpage";
	}
	
	@PostMapping("/reg_submit")
	public String regSubmit(@ModelAttribute Member member, 
	        BindingResult result, Model model) {
	    
	    Member existingMember = memberService.findMemberByAccount(member.getEmail());
	    
	    if (existingMember != null) {
	        model.addAttribute("error", "帳號已經存在，請選擇其他帳號"); //帳號已存在
	        return "registerpage"; // 返回註冊頁面
	    }
	    
	    /* 表單錯誤
	    if (result.hasErrors()) {
	        System.out.println(result.getAllErrors().toString());
	        return "registerpage";
	    }*/

	    memberService.addMember(member);//新增註冊會員

	   
	    return "redirect:/loginpage";   // 返回登錄頁面
	}
	
	
	@GetMapping("/loginpage")
	public String login(Model model) {
		model.addAttribute("member", new Member());
		return "loginpage";
	}
	
	
	@PostMapping("/login_submit")
	public String loginSubmit(@ModelAttribute Member member, 
			BindingResult result, Model model, HttpSession session) {
		
		
		  //獲取資料庫中的會員資料
	    Member existingMember = memberService.findMemberByAccount(member.getEmail());
	    
	    // 驗證帳號
	    if (existingMember == null) {
	        
	        model.addAttribute("error", "帳號或密碼錯誤！");
	        return "loginpage"; // 返回頁面 顯示提示
	    } 
	    //驗證密碼
	    if (!BCrypt.checkpw(member.getPassword(), existingMember.getPassword())) {
	        
	        model.addAttribute("error", "帳號或密碼錯誤！");
	        return "loginpage"; // 返回頁面 顯示提示
	    }else {
			session.setAttribute("member", existingMember);//加到session
		}

		model.addAttribute("member", existingMember);//加到資料庫
		
		return "redirect:/main";
	
	}
	
	@GetMapping("/main")
	public String mainPage(HttpSession session, HttpServletResponse response) {
			
		
	    // 檢查是否有登錄信息
	    if (session.getAttribute("member") == null) {
	        // 如果沒有登錄，重定向到登錄頁面
	        return "redirect:/loginpage";
	    }
	    
	    return "main";//有session
	}
	
		//登出
	 @RequestMapping("/logout")
	    public String logout(HttpSession session) {
	        session.invalidate();  
            return "redirect:/loginpage";
	    }
		
	@GetMapping("/forgotPassword")
	public String showForgotPasswordPage() {
	    return "forgotPassword"; 
	}
	
	
}
