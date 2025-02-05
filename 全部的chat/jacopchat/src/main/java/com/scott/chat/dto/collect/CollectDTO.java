package com.scott.chat.dto.collect;

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
   
   // 無參構造函數
   public CollectDTO() {
   }
   
   // 全參構造函數
   public CollectDTO(Integer collectId, Integer postId, Integer collecterId,
                    String collectTime, String note, String postContent,
                    String postPhotoUrl, Integer postOwnerId, String postOwnerName,
                    String postOwnerAvatar, Integer collectedCount, boolean canRemove) {
       this.collectId = collectId;
       this.postId = postId;
       this.collecterId = collecterId;
       this.collectTime = collectTime;
       this.note = note;
       this.postContent = postContent;
       this.postPhotoUrl = postPhotoUrl;
       this.postOwnerId = postOwnerId;
       this.postOwnerName = postOwnerName;
       this.postOwnerAvatar = postOwnerAvatar;
       this.collectedCount = collectedCount;
       this.canRemove = canRemove;
   }
   
   // Getter方法
   public Integer getCollectId() {
       return collectId;
   }
   
   public Integer getPostId() {
       return postId;
   }
   
   public Integer getCollecterId() {
       return collecterId;
   }
   
   public String getCollectTime() {
       return collectTime;
   }
   
   public String getNote() {
       return note;
   }
   
   public String getPostContent() {
       return postContent;
   }
   
   public String getPostPhotoUrl() {
       return postPhotoUrl;
   }
   
   public Integer getPostOwnerId() {
       return postOwnerId;
   }
   
   public String getPostOwnerName() {
       return postOwnerName;
   }
   
   public String getPostOwnerAvatar() {
       return postOwnerAvatar;
   }
   
   public Integer getCollectedCount() {
       return collectedCount;
   }
   
   public boolean isCanRemove() {
       return canRemove;
   }
   
   // Setter方法
   public void setCollectId(Integer collectId) {
       this.collectId = collectId;
   }
   
   public void setPostId(Integer postId) {
       this.postId = postId;
   }
   
   public void setCollecterId(Integer collecterId) {
       this.collecterId = collecterId;
   }
   
   public void setCollectTime(String collectTime) {
       this.collectTime = collectTime;
   }
   
   public void setNote(String note) {
       this.note = note;
   }
   
   public void setPostContent(String postContent) {
       this.postContent = postContent;
   }
   
   public void setPostPhotoUrl(String postPhotoUrl) {
       this.postPhotoUrl = postPhotoUrl;
   }
   
   public void setPostOwnerId(Integer postOwnerId) {
       this.postOwnerId = postOwnerId;
   }
   
   public void setPostOwnerName(String postOwnerName) {
       this.postOwnerName = postOwnerName;
   }
   
   public void setPostOwnerAvatar(String postOwnerAvatar) {
       this.postOwnerAvatar = postOwnerAvatar;
   }
   
   public void setCollectedCount(Integer collectedCount) {
       this.collectedCount = collectedCount;
   }
   
   public void setCanRemove(boolean canRemove) {
       this.canRemove = canRemove;
   }
}