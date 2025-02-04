package com.scott.chat.dto.collect;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectDTO {
    private Integer collectId;
    private Integer postId;
    private Integer collecterId;    // 收藏者ID
    
    // 收藏時間和筆記
    private String collectTime;
    private String note;            // 收藏備註（可選）
    
    // 貼文預覽資訊 - 確保顯示原始發文者資訊
    private String postContent;
    private String postPhotoUrl;    // 首張圖片預覽
    private Integer postOwnerId;    // 原始貼文作者ID
    private String postOwnerName;   // 原始貼文作者名稱
    private String postOwnerAvatar; // 原始貼文作者頭像
    
    // 統計資訊
    private Integer collectedCount;  // 被收藏次數
    
    // 權限控制
    private boolean canRemove;      // 是否可以取消收藏（只有收藏者自己可以）
}