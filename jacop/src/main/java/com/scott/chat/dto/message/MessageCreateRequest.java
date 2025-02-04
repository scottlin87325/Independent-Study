package com.scott.chat.dto.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequest {
    private Integer postId;             // 貼文ID
    private Integer userId;             // 使用者ID
    private String message;               // 留言內容
    private MultipartFile messageFile;  // 可選的附加檔案
}