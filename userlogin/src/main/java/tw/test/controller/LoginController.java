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
@SessionAttributes("member") //為了多次請求
public class LoginController {
	@Autowired
	private MemberService memberService;

	
	
	
	@RequestMapping("/registerpage")
	public String reg(Model model) {
		Member member = new Member();
		model.addAttribute("member", member);
		return "registerpage";
	}
	
	@PostMapping("/reg_submit")
	public String regSubmit(@ModelAttribute Member member, 
	        BindingResult result, Model model) {
	    // 檢查帳號是否已經存在
	    Member existingMember = memberService.findMemberByAccount(member.getAccount());
	    
	    if (existingMember != null) {
	        // 如果帳號已存在，返回錯誤訊息
	        model.addAttribute("error", "帳號已經存在，請選擇其他帳號");
	        return "registerpage"; // 返回註冊頁面，顯示錯誤訊息
	    }

	    // 如果沒有錯誤，保存會員資料
	    if (result.hasErrors()) {
	        System.out.println(result.getAllErrors().toString());
	        return "registerpage";  // 返回註冊頁面，提示錯誤
	    }

	    memberService.addMember(member);

	    // 註冊成功後，返回登錄頁面
	    return "redirect:/loginpage";  
	}
	
	@GetMapping("/loginpage")
	public String login(Model model) {
		model.addAttribute("member", new Member());
		return "loginpage";
	}
	
	
	@PostMapping("/login_submit")
	public String loginSubmit(@ModelAttribute Member member, 
			BindingResult result, Model model, HttpSession session) {
		
		
		  // 2. 獲取資料庫中的會員資料
	    Member existingMember = memberService.findMemberByAccount(member.getAccount());
	    
	    if (existingMember == null) {
	        // 帳號不存在
	        model.addAttribute("error", "該帳號不存在！");
	        return "loginpage"; // 返回錯誤頁面並顯示提示
	    } 
	    
	    // 2. 提取資料庫中的加密密碼
       

	    // 3. 驗證密碼
	    if (!BCrypt.checkpw(member.getPasswd(), existingMember.getPasswd())) {
	        // 密碼錯誤
	        model.addAttribute("error", "密碼錯誤！");
	        return "loginpage"; // 返回錯誤頁面並顯示提示
	    }else {
			session.setAttribute("member", existingMember);
		}

		model.addAttribute("member", existingMember);
		
		return "redirect:/main";
	
	}
	
	@GetMapping("/main")
	public String mainPage(HttpSession session, HttpServletResponse response) {
		// 防止瀏覽器快取
	    response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
	    response.setHeader("Pragma", "no-cache");
	    response.setDateHeader("Expires", 0);
		
		
	    // 檢查是否有登錄信息
	    if (session.getAttribute("member") == null) {
	        // 如果沒有登錄，重定向到登錄頁面
	        return "redirect:/loginpage";
	    }
	    // 如果已經登錄，顯示主頁
	    return "main";
	}
	
	
	
	
	
	
	
	
	
	
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
