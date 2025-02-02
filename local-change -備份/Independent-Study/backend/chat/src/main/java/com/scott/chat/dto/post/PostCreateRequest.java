package com.scott.chat.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
// 創建貼文的請求對象，用於接收前端創建貼文的數據
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {
    private String content;           // 貼文內容
    private Integer posterId;         // 發文者ID
    private MultipartFile[] photos;   // 上傳的圖片文件數組
}