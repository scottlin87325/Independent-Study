package com.scott.chat.dto.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;
import com.scott.chat.dto.message.MessageDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private PostDTO post;               // 貼文資訊
    private List<MessageDTO> messages;  // 留言列表
    private boolean hasMoreMessages;    // 是否還有更多留言
    private int totalMessages;          // 總留言數
}