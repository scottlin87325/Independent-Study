package com.scott.chat.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scott.chat.model.Member;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.service.MemberService;
import com.scott.chat.dto.common.ApiResponse;
import com.scott.chat.exception.UnauthorizedException;

// REST風格的會員控制器
@RestController
@RequestMapping("/api/member")
public class MemberController {

   @Autowired
   private MemberRepository memberRepository;
   
   @Autowired
   private MemberService memberService;

   // 獲取指定ID的會員信息
   @GetMapping("/{id}")
   public ResponseEntity<Map<String, Object>> getMember(@PathVariable Integer id) {
       return memberRepository.findById(id)
           .map(member -> {
               Map<String, Object> response = new HashMap<>();
               response.put("membername", member.getMembername());
               response.put("memberphoto", member.getMemberphoto() != null ? 
                   Base64.getEncoder().encodeToString(member.getMemberphoto()) : null);
               response.put("email", member.getEmail());
               response.put("postcount", member.getPostcount());
               response.put("introduce", member.getIntroduce());
               return ResponseEntity.ok(response);
           })
           .orElse(ResponseEntity.notFound().build());
   }
   
   // 獲取當前登入會員的信息
   @GetMapping("/current")
   public ResponseEntity<Map<String, Object>> getCurrentMember(Authentication authentication) {
       // 檢查是否已登入
       if (authentication == null || !authentication.isAuthenticated()) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }
       
       try {
           Member member = memberService.findMemberByAccount(authentication.getName());
           if (member == null) {
               return ResponseEntity.notFound().build();
           }
           
           // 準備響應數據
           Map<String, Object> response = new HashMap<>();
           response.put("memberId", member.getMemberid());
           response.put("membername", member.getMembername());
           response.put("email", member.getEmail());
           response.put("memberphoto", member.getMemberphoto() != null ? 
               Base64.getEncoder().encodeToString(member.getMemberphoto()) : null);
           response.put("postcount", member.getPostcount());
           response.put("introduce", member.getIntroduce());
           
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }
   
   // 更新會員資料
   @PutMapping("/profile")
   public ResponseEntity<ApiResponse<String>> updateProfile(
           @RequestBody Member updatedMember,
           Authentication authentication) {
       
       // 檢查權限
       if (authentication == null || !authentication.isAuthenticated()) {
           throw new UnauthorizedException("請先登入");
       }
       
       try {
           Member currentMember = memberService.findMemberByAccount(authentication.getName());
           if (currentMember == null) {
               return ResponseEntity.notFound().build();
           }
           
           // 只更新允許的欄位
           currentMember.setMembername(updatedMember.getMembername());
           currentMember.setIntroduce(updatedMember.getIntroduce());
           if (updatedMember.getMemberphoto() != null) {
               currentMember.setMemberphoto(updatedMember.getMemberphoto());
           }
           
           memberService.updateMember(currentMember);
           
           ApiResponse<String> response = new ApiResponse<>();
           response.setStatus(200);
           response.setMessage("個人資料更新成功");
           
           return ResponseEntity.ok(response);
               
       } catch (Exception e) {
           ApiResponse<String> response = new ApiResponse<>();
           response.setStatus(500);
           response.setMessage("更新失敗: " + e.getMessage());
           
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(response);
       }
   }
   
   // 檢查用戶是否已認證
   @GetMapping("/check-auth")
   public ResponseEntity<Boolean> checkAuthentication(Authentication authentication) {
       if (authentication != null && authentication.isAuthenticated() 
               && !authentication.getPrincipal().equals("anonymousUser")) {
           return ResponseEntity.ok(true);
       }
       return ResponseEntity.ok(false);
   }
}