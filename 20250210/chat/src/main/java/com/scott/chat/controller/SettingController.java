package com.scott.chat.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scott.chat.model.Member;
import com.scott.chat.service.SettingService;

@RestController
public class SettingController {
    @Autowired
    private SettingService settingService;
    
    @GetMapping("/api/member/current")
    public ResponseEntity<Member> getCurrentMember() {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       String email = auth.getName();
       Member member = settingService.findByEmail(email);
       
       if(member.getMemberphoto() != null) {
           member.setMemberphotobase64(Base64.getEncoder().encodeToString(member.getMemberphoto()));
       }
       
       return ResponseEntity.ok(member);
    }

    @PostMapping("/api/member/update") 
    public ResponseEntity<Member> updateMember(@RequestBody Map<String, String> updates, Authentication auth) {
       String email = auth.getName();
       Member member = settingService.findByEmail(email);
       
       if (member != null) {
           // 修改這裡的判斷邏輯，允許空值更新
           member.setMembername(updates.getOrDefault("membername", member.getMembername()));
           member.setIntroduce(updates.getOrDefault("introduce", member.getIntroduce()));
           member.setGender(updates.getOrDefault("gender", member.getGender()));
           member.setBirthday(updates.getOrDefault("birthday", member.getBirthday()));
           member.setTelephone(updates.getOrDefault("telephone", member.getTelephone()));
           
           settingService.save(member);
           return ResponseEntity.ok(member);
       }
       return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/member/update-photo")
    public ResponseEntity<?> updatePhoto(@RequestParam("memberphotofile") MultipartFile file, Authentication auth) {
        try {
            // 基本驗證
            if (file == null || file.isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body("請選擇要上傳的圖片");
            }

            // 檢查檔案大小（限制為 5MB）
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity
                    .badRequest()
                    .body("圖片大小不能超過 5MB");
            }

            // 檢查檔案類型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity
                    .badRequest()
                    .body("只能上傳圖片檔案");
            }

            String email = auth.getName();
            Member member = settingService.findByEmail(email);
            
            if (member == null) {
                return ResponseEntity
                    .notFound()
                    .build();
            }

            try {
                // 先將圖片轉換為 byte array
                byte[] photoData = file.getBytes();
                
                // 檢查是否成功獲取圖片數據
                if (photoData == null || photoData.length == 0) {
                    throw new IllegalArgumentException("無法讀取圖片數據");
                }

                // 使用現有的 setMemberphotofile 方法
                member.setMemberphotofile(file);
                
                // 確保圖片數據被正確設置
                if (member.getMemberphoto() == null) {
                    return ResponseEntity
                        .internalServerError()
                        .body("圖片處理失敗");
                }

                // 儲存更新後的會員資料
                settingService.save(member);
                
                // 重新設置 base64 字串
                if (member.getMemberphoto() != null) {
                    member.setMemberphotobase64(
                        Base64.getEncoder().encodeToString(member.getMemberphoto())
                    );
                }

                return ResponseEntity.ok(member);
                
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity
                    .internalServerError()
                    .body("圖片處理失敗：" + e.getMessage());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .internalServerError()
                .body("圖片上傳失敗：" + e.getMessage());
        }
    }
    
}

