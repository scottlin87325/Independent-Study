package com.scott.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scott.chat.service.PostService;

@Controller
@RequestMapping("/main")
public class MainController {
    
	@Autowired
	private PostService postService;
	
	@GetMapping("/post")
	public String getPost() {
		return "redirect:/Home/post/1/1";
	}
}
