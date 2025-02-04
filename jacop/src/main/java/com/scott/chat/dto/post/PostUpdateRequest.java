package com.scott.chat.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    private Integer postId;     // 要更新的貼文ID
    private String content;     // 更新後的內容
}