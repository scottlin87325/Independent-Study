package com.scott.chat.dto.comment;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;  // 添加這行

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private Integer postId;
    private Integer memberId;
    
    // 評論內容和時間
    private String content;
    private String createdAt;
    
    // 評論者資訊
    private String memberName;
    private String memberAvatar;
    
    // 權限控制
    private boolean isOwnComment;
    
    // 統計資訊
    private Integer likeCount;
    private boolean isLiked;
    
    // 互動資訊（可選）
    private String lastEditTime;
    private List<ReplyDTO> replies;  // 如果支援巢狀回覆
    
    @Data
    @Builder
    public static class ReplyDTO {
        private Long id;
        private String content;
        private String createdAt;
        private Integer memberId;
        private String memberName;
        private String memberAvatar;
        private boolean isOwnReply;
    }
}