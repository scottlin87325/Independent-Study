package com.scott.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
	/**
     * 首頁跳轉
     */
    @GetMapping("/home")
    public String showHomePage() {
        return "Home";
    }
    
    /**
     * 搜尋頁面跳轉
     */
    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }
    
    /**
     * 個人資料頁面跳轉
     */
    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }
    
    /**
     * 處理設定頁面的跳轉
     */
    @GetMapping("/settings")
    public String showSettingsPage() {
        return "/setting";  // 返回設定頁面
    }
    

}
