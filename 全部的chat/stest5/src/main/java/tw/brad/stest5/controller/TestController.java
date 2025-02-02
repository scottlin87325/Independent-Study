package tw.brad.stest5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpServletRequest;


@RequestMapping("/test")
@Controller
public class TestController {
	
	@GetMapping("/test1")
	public String test1() {
		return "redirect:/test/test2?x=5&y=4";
	}
	
	@GetMapping("/test2")
	public String test2(@RequestParam Integer x, @RequestParam Integer y) {
		System.out.println(x);
		System.out.println(y);
		return "test2";
	}
	
	@GetMapping("/test3")
	public String test3(HttpServletRequest request) {
		String redirect = String.format("redirect:/test/test2?x=%s&y=%s",
				request.getParameter("x"),
				request.getParameter("y"));
		return redirect;
	}
	
	@GetMapping("/test4")
	public String test4(@RequestParam Integer x, @RequestParam Integer y,Model model) {
		return "test4";
		
	}
	
}
