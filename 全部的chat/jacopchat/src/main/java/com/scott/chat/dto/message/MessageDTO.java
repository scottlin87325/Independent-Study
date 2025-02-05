package com.scott.chat.dto.message;

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
   private Integer likedCount = 0;    // 讚數，預設0
   private boolean isLiked = false;   // 當前用戶是否點讚
   private boolean isRead = false;    // 是否已讀
   private String readTime;           // 讀取時間
   
   // 權限控制
   private boolean isCurrentUser = false;  // 是否為當前用戶的訊息
   private boolean canEdit = false;        // 是否可以編輯
   private boolean canDelete = false;      // 是否可以刪除

   // 無參構造函數
   public MessageDTO() {
   }
   
   // 全參構造函數
   public MessageDTO(Integer messageId, Integer postId, String message, String messageTime,
                    String messageFileUrl, Integer userId, String userName, String userAvatar,
                    Integer likedCount, boolean isLiked, boolean isRead, String readTime,
                    boolean isCurrentUser, boolean canEdit, boolean canDelete) {
       this.messageId = messageId;
       this.postId = postId;
       this.message = message;
       this.messageTime = messageTime;
       this.messageFileUrl = messageFileUrl;
       this.userId = userId;
       this.userName = userName;
       this.userAvatar = userAvatar;
       this.likedCount = likedCount;
       this.isLiked = isLiked;
       this.isRead = isRead;
       this.readTime = readTime;
       this.isCurrentUser = isCurrentUser;
       this.canEdit = canEdit;
       this.canDelete = canDelete;
   }
   
   // Getter方法
   public Integer getMessageId() {
       return messageId;
   }
   
   public Integer getPostId() {
       return postId;
   }
   
   public String getMessage() {
       return message;
   }
   
   public String getMessageTime() {
       return messageTime;
   }
   
   public String getMessageFileUrl() {
       return messageFileUrl;
   }
   
   public Integer getUserId() {
       return userId;
   }
   
   public String getUserName() {
       return userName;
   }
   
   public String getUserAvatar() {
       return userAvatar;
   }
   
   public Integer getLikedCount() {
       return likedCount;
   }
   
   public boolean isLiked() {
       return isLiked;
   }
   
   public boolean isRead() {
       return isRead;
   }
   
   public String getReadTime() {
       return readTime;
   }
   
   public boolean isCurrentUser() {
       return isCurrentUser;
   }
   
   public boolean isCanEdit() {
       return canEdit;
   }
   
   public boolean isCanDelete() {
       return canDelete;
   }
   
   // Setter方法
   public void setMessageId(Integer messageId) {
       this.messageId = messageId;
   }
   
   public void setPostId(Integer postId) {
       this.postId = postId;
   }
   
   public void setMessage(String message) {
       this.message = message;
   }
   
   public void setMessageTime(String messageTime) {
       this.messageTime = messageTime;
   }
   
   public void setMessageFileUrl(String messageFileUrl) {
       this.messageFileUrl = messageFileUrl;
   }
   
   public void setUserId(Integer userId) {
       this.userId = userId;
   }
   
   public void setUserName(String userName) {
       this.userName = userName;
   }
   
   public void setUserAvatar(String userAvatar) {
       this.userAvatar = userAvatar;
   }
   
   public void setLikedCount(Integer likedCount) {
       this.likedCount = likedCount;
   }
   
   public void setLiked(boolean liked) {
       isLiked = liked;
   }
   
   public void setRead(boolean read) {
       isRead = read;
   }
   
   public void setReadTime(String readTime) {
       this.readTime = readTime;
   }
   
   public void setCurrentUser(boolean currentUser) {
       isCurrentUser = currentUser;
   }
   
   public void setCanEdit(boolean canEdit) {
       this.canEdit = canEdit;
   }
   
   public void setCanDelete(boolean canDelete) {
       this.canDelete = canDelete;
   }
}