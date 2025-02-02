package com.scott.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scott.chat.service.TokenService;
import com.scott.chat.model.Member;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.model.TokenRecord;
import com.scott.chat.model.PasswordReset;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/token")
public class TokenController {

   @Autowired
   private TokenService tokenService;

   @Autowired
   private MemberRepository memberRepository;
   
   // 用於生成密碼重置的 Token
   @PostMapping("/generate/{email}")
   public ResponseEntity<Map<String, String>> generateResetToken(@PathVariable String email) {
       try {
           String token = tokenService.generatePasswordResetToken(email);
           // 返回 JSON 格式的 Map
           Map<String, String> response = new HashMap<>();
           response.put("token", token);
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
               .body(Collections.singletonMap("error", "無法生成重置密碼 Token"));
       }
   }

   // 用於檢查 Token 是否有效
   @GetMapping("/validate/{token}")
   public ResponseEntity<Boolean> validateToken(@PathVariable String token) {
       try {
           boolean isValid = tokenService.validateToken(token);
           return ResponseEntity.ok(isValid);
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
       }
   }

   // 用於通過 Token 查找相關會員
   @GetMapping("/member/{token}")
   public ResponseEntity<Member> getMemberByToken(@PathVariable String token) {
       try {
           Optional<Member> member = tokenService.getMemberByToken(token);
           return member.map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }

   // 用於重置密碼
   @PostMapping("/reset-password/{token}")
   public ResponseEntity<Map<String, String>> resetPassword(
           @PathVariable String token,
           @RequestParam String newPassword) {
       try {
           boolean success = tokenService.updatePasswordByToken(token, newPassword);
           Map<String, String> response = new HashMap<>();
           if (success) {
               response.put("message", "密碼重置成功");
               return ResponseEntity.ok(response);
           } else {
               response.put("error", "密碼重置失敗");
               return ResponseEntity.badRequest().body(response);
           }
       } catch (Exception e) {
           Map<String, String> response = new HashMap<>();
           response.put("error", "密碼重置過程發生錯誤");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
   }

   // 檢查 Token 是否過期
   @GetMapping("/check-expiry/{token}")
   public ResponseEntity<Map<String, Object>> checkTokenExpiry(@PathVariable String token) {
       try {
           Map<String, Object> response = new HashMap<>();
           boolean isValid = tokenService.validateToken(token);
           response.put("valid", isValid);
           if (!isValid) {
               response.put("message", "Token 已過期或無效");
           }
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           Map<String, Object> response = new HashMap<>();
           response.put("error", "檢查 Token 時發生錯誤");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
   }

   // 清理過期的 Token
   @DeleteMapping("/cleanup-expired")
   public ResponseEntity<Map<String, String>> cleanupExpiredTokens() {
       try {
           tokenService.cleanupExpiredTokens();
           Map<String, String> response = new HashMap<>();
           response.put("message", "過期的 Token 已清理完成");
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           Map<String, String> response = new HashMap<>();
           response.put("error", "清理過期 Token 時發生錯誤");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
   }

   // 驗證 Email 與 Token 的對應關係
   @GetMapping("/verify-email/{token}/{email}")
   public ResponseEntity<Map<String, Object>> verifyEmailToken(
           @PathVariable String token,
           @PathVariable String email) {
       try {
           Optional<Member> member = tokenService.getMemberByToken(token);
           Map<String, Object> response = new HashMap<>();
           
           if (member.isPresent() && member.get().getEmail().equals(email)) {
               response.put("valid", true);
               response.put("message", "Token 與 Email 匹配");
           } else {
               response.put("valid", false);
               response.put("message", "Token 與 Email 不匹配");
           }
           
           return ResponseEntity.ok(response);
       } catch (Exception e) {
           Map<String, Object> response = new HashMap<>();
           response.put("error", "驗證過程發生錯誤");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
   }
}