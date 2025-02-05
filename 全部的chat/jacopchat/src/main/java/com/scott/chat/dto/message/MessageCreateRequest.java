package com.scott.chat.dto.message;

import org.springframework.web.multipart.MultipartFile;

public class MessageCreateRequest {
   private Integer postId;             // 貼文ID
   private Integer userId;             // 使用者ID
   private String message;             // 留言內容
   private MultipartFile messageFile;  // 可選的附加檔案
   
   // 無參構造函數
   public MessageCreateRequest() {
   }
   
   // 全參構造函數
   public MessageCreateRequest(Integer postId, Integer userId, String message, MultipartFile messageFile) {
       this.postId = postId;
       this.userId = userId;
       this.message = message;
       this.messageFile = messageFile;
   }
   
   // Getter方法
   public Integer getPostId() {
       return postId;
   }
   
   public Integer getUserId() {
       return userId;
   }
   
   public String getMessage() {
       return message;
   }
   
   public MultipartFile getMessageFile() {
       return messageFile;
   }
   
   // Setter方法
   public void setPostId(Integer postId) {
       this.postId = postId;
   }
   
   public void setUserId(Integer userId) {
       this.userId = userId;
   }
   
   public void setMessage(String message) {
       this.message = message;
   }
   
   public void setMessageFile(MultipartFile messageFile) {
       this.messageFile = messageFile;
   }
}