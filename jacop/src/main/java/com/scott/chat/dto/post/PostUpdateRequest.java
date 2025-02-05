package com.scott.chat.dto.post;

public class PostUpdateRequest {
   private Integer postId;     // 要更新的貼文ID
   private String content;     // 更新後的內容
   
   // 無參構造函數
   public PostUpdateRequest() {
   }
   
   // 全參構造函數
   public PostUpdateRequest(Integer postId, String content) {
       this.postId = postId;
       this.content = content;
   }
   
   // Getter方法
   public Integer getPostId() {
       return postId;
   }
   
   public String getContent() {
       return content;
   }
   
   // Setter方法 
   public void setPostId(Integer postId) {
       this.postId = postId;
   }
   
   public void setContent(String content) {
       this.content = content;
   }
}