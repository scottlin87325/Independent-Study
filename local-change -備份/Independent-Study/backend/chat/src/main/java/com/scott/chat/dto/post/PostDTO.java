package com.scott.chat.dto.post;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
// 貼文數據傳輸對象，用於在系統層級之間傳遞貼文相關數據
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    // 基本資訊
    private Integer postId;           // 貼文ID
    private Integer posterId;         // 發文者ID
    private String postTime;          // 發文時間
    private String postContent;       // 貼文內容
    
    // 發文者資訊
    private String posterName;        // 發文者名稱
    private String posterAvatar;      // 發文者頭像
    private boolean ownPost;          // 是否為自己的貼文
    
    // 統計資訊 - 使用 @Default 設置預設值
    @Default
    private Integer likedCount = 0;    // 讚數
    @Default
    private Integer messageCount = 0;  // 留言數
    @Default
    private Integer collectCount = 0;  // 收藏數
    
    // 互動狀態
    @Default
    private boolean isLiked = false;     // 是否已點讚
    @Default
    private boolean isCollected = false;  // 是否已收藏
    
    // 圖片資訊
    private List<String> photoUrls;      // 貼文圖片URL列表
    
    // 最新留言預覽
    private List<CommentPreviewDTO> recentComments;  // 最近的留言列表
    
    // 權限控制標記
    @Default
    private boolean canEdit = false;    // 是否可以編輯
    @Default
    private boolean canDelete = false;  // 是否可以刪除
    
    // 留言預覽內部類
    @Data
    @Builder
    public static class CommentPreviewDTO {
        private Long commentId;      // 留言ID
        private String content;      // 留言內容
        private String memberName;   // 留言者名稱
        private String createdAt;    // 留言時間
    }
}