package com.scott.chat.dto.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
@Data  // Lombok注解：自動生成getter、setter等方法
@NoArgsConstructor  // 生成無參構造函數
@AllArgsConstructor  // 生成全參構造函數
@Builder  // 使用建造者模式
public class MessageDTO {
    private Integer messageId;    // 訊息ID
    private Integer postId;       // 所屬貼文ID
    
    // 訊息內容相關
    private String message;       // 訊息內容
    private String messageTime;   // 發送時間
    private String messageFileUrl;// 附件檔案URL
    
    // 發送者資訊
    private Integer userId;       // 發送者ID
    private String userName;      // 發送者名稱
    private String userAvatar;    // 發送者頭像
    
    // 統計和狀態資訊
    @Default
    private Integer likedCount = 0;    // 讚數，預設0
    @Default
    private boolean isLiked = false;   // 當前用戶是否點讚
    @Default
    private boolean isRead = false;    // 是否已讀
    private String readTime;           // 讀取時間
    
    // 權限控制
    @Default
    private boolean isCurrentUser = false;  // 是否為當前用戶的訊息
    @Default
    private boolean canEdit = false;        // 是否可以編輯
    @Default
    private boolean canDelete = false;      // 是否可以刪除
}