package com.scott.chat.dto.post;

import java.util.List;

/**
* 貼文資料傳輸物件(DTO)
* 用於前後端傳遞貼文相關資訊
*/
public class PostDTO {
   
   // === 基本資訊 ===
   private Integer postId;          // 貼文編號  
   private Integer posterId;        // 發文者編號
   private String postTime;         // 發文時間
   private String postContent;      // 貼文內容
   
   // === 發文者資訊 ===
   private String posterName;       // 發文者名稱
   private String posterAvatar;     // 發文者頭像網址
   private boolean ownPost;         // 是否為當前用戶的貼文
   
   // === 統計資訊 ===
   private Integer likedCount = 0;    // 獲得的讚數
   private Integer messageCount = 0;  // 留言數量
   private Integer collectCount = 0;  // 收藏數量
   
   // === 互動狀態 ===
   private boolean isLiked = false;     // 當前用戶是否已按讚
   private boolean isCollected = false;  // 當前用戶是否已收藏
   
   // === 媒體內容 ===
   private List<String> photoUrls;      // 貼文附帶的照片URL列表
   
   // === 留言預覽 === 
   private List<CommentPreviewDTO> recentComments;  // 最新留言預覽列表
   
   // === 權限控制 ===
   private boolean canEdit = false;    // 當前用戶是否可編輯此貼文
   private boolean canDelete = false;  // 當前用戶是否可刪除此貼文

   /**
    * 預設建構子
    */
   public PostDTO() {}
   
   /**
    * 完整建構子
    */
   public PostDTO(Integer postId, Integer posterId, String postTime, String postContent,
                 String posterName, String posterAvatar, boolean ownPost,
                 Integer likedCount, Integer messageCount, Integer collectCount,
                 boolean isLiked, boolean isCollected, List<String> photoUrls,
                 List<CommentPreviewDTO> recentComments, boolean canEdit, boolean canDelete) {
       this.postId = postId;
       this.posterId = posterId;
       this.postTime = postTime;
       this.postContent = postContent;
       this.posterName = posterName;
       this.posterAvatar = posterAvatar;
       this.ownPost = ownPost;
       this.likedCount = likedCount;
       this.messageCount = messageCount;
       this.collectCount = collectCount;
       this.isLiked = isLiked;
       this.isCollected = isCollected;
       this.photoUrls = photoUrls;
       this.recentComments = recentComments;
       this.canEdit = canEdit;
       this.canDelete = canDelete;
   }
   
   // === Getters ===
   public Integer getPostId() {
       return postId;
   }
   
   public Integer getPosterId() {
       return posterId;
   }
   
   public String getPostTime() {
       return postTime;
   }
   
   public String getPostContent() {
       return postContent;
   }
   
   public String getPosterName() {
       return posterName;
   }
   
   public String getPosterAvatar() {
       return posterAvatar;
   }
   
   public boolean isOwnPost() {
       return ownPost;
   }
   
   public Integer getLikedCount() {
       return likedCount;
   }
   
   public Integer getMessageCount() {
       return messageCount;
   }
   
   public Integer getCollectCount() {
       return collectCount;
   }
   
   public boolean isLiked() {
       return isLiked;
   }
   
   public boolean isCollected() {
       return isCollected;
   }
   
   public List<String> getPhotoUrls() {
       return photoUrls;
   }
   
   public List<CommentPreviewDTO> getRecentComments() {
       return recentComments;
   }
   
   public boolean isCanEdit() {
       return canEdit;
   }
   
   public boolean isCanDelete() {
       return canDelete;
   }
   
   // === Setters ===
   public void setPostId(Integer postId) {
       this.postId = postId;
   }
   
   public void setPosterId(Integer posterId) {
       this.posterId = posterId;
   }
   
   public void setPostTime(String postTime) {
       this.postTime = postTime;
   }
   
   public void setPostContent(String postContent) {
       this.postContent = postContent;
   }
   
   public void setPosterName(String posterName) {
       this.posterName = posterName;
   }
   
   public void setPosterAvatar(String posterAvatar) {
       this.posterAvatar = posterAvatar;
   }
   
   public void setOwnPost(boolean ownPost) {
       this.ownPost = ownPost;
   }
   
   public void setLikedCount(Integer likedCount) {
       this.likedCount = likedCount;
   }
   
   public void setMessageCount(Integer messageCount) {
       this.messageCount = messageCount;
   }
   
   public void setCollectCount(Integer collectCount) {
       this.collectCount = collectCount;
   }
   
   public void setLiked(boolean liked) {
       isLiked = liked;
   }
   
   public void setCollected(boolean collected) {
       isCollected = collected;
   }
   
   public void setPhotoUrls(List<String> photoUrls) {
       this.photoUrls = photoUrls;
   }
   
   public void setRecentComments(List<CommentPreviewDTO> recentComments) {
       this.recentComments = recentComments;
   }
   
   public void setCanEdit(boolean canEdit) {
       this.canEdit = canEdit;
   }
   
   public void setCanDelete(boolean canDelete) {
       this.canDelete = canDelete;
   }
   
   /**
    * 留言預覽資料類別
    * 用於顯示貼文下方的簡短留言預覽
    */
   public static class CommentPreviewDTO {
       private Long commentId;     // 留言編號
       private String content;     // 留言內容
       private String memberName;  // 留言者名稱
       private String createdAt;   // 留言時間
       
       /**
        * 預設建構子
        */
       public CommentPreviewDTO() {}
       
       /**
        * 完整建構子
        */
       public CommentPreviewDTO(Long commentId, String content, String memberName, String createdAt) {
           this.commentId = commentId;
           this.content = content;
           this.memberName = memberName;
           this.createdAt = createdAt;
       }
       
       // === Getters ===
       public Long getCommentId() {
           return commentId;
       }
       
       public String getContent() {
           return content;
       }
       
       public String getMemberName() {
           return memberName;
       }
       
       public String getCreatedAt() {
           return createdAt;
       }
       
       // === Setters ===
       public void setCommentId(Long commentId) {
           this.commentId = commentId;
       }
       
       public void setContent(String content) {
           this.content = content;
       }
       
       public void setMemberName(String memberName) {
           this.memberName = memberName;
       }
       
       public void setCreatedAt(String createdAt) {
           this.createdAt = createdAt;
       }
   }
}