package com.scott.chat.dto.post;

import org.springframework.web.multipart.MultipartFile;

// 創建貼文的請求對象，用於接收前端創建貼文的數據
public class PostCreateRequest {
   private String content;           // 貼文內容
   private Integer posterId;         // 發文者ID 
   private MultipartFile[] photos;   // 上傳的圖片文件數組
   
   // 無參構造函數
   public PostCreateRequest() {
   }
   
   // 全參構造函數
   public PostCreateRequest(String content, Integer posterId, MultipartFile[] photos) {
       this.content = content;
       this.posterId = posterId;
       this.photos = photos;
   }
   
   // Getter方法
   public String getContent() {
       return content;
   }
   
   public Integer getPosterId() {
       return posterId;
   }
   
   public MultipartFile[] getPhotos() {
       return photos;
   }
   
   // Setter方法
   public void setContent(String content) {
       this.content = content;
   }
   
   public void setPosterId(Integer posterId) {
       this.posterId = posterId;
   }
   
   public void setPhotos(MultipartFile[] photos) {
       this.photos = photos;
   }
}