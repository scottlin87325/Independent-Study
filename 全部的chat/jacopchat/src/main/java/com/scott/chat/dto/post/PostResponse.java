package com.scott.chat.dto.post;

import java.util.List;
import com.scott.chat.dto.message.MessageDTO;

/**
* 貼文回應物件
* 包含貼文內容及相關訊息
*/
public class PostResponse {
   // 貼文基本資料
   private PostDTO post;
   // 留言清單
   private List<MessageDTO> messages;  
   // 是否還有更多未載入的留言
   private boolean hasMoreMessages;
   // 此貼文的總留言數
   private int totalMessages;

   /**
    * 預設建構子
    */
   public PostResponse() {}

   /**
    * 含參數建構子
    * @param post 貼文資料
    * @param messages 留言列表
    * @param hasMoreMessages 是否有更多留言
    * @param totalMessages 總留言數
    */
   public PostResponse(PostDTO post, List<MessageDTO> messages, boolean hasMoreMessages, int totalMessages) {
       this.post = post;
       this.messages = messages;
       this.hasMoreMessages = hasMoreMessages;
       this.totalMessages = totalMessages;
   }

   // 取得貼文資料
   public PostDTO getPost() {
       return post;
   }

   // 設定貼文資料
   public void setPost(PostDTO post) {
       this.post = post;
   }

   // 取得留言列表
   public List<MessageDTO> getMessages() {
       return messages;
   }

   // 設定留言列表
   public void setMessages(List<MessageDTO> messages) {
       this.messages = messages;
   }

   // 檢查是否還有更多留言
   public boolean isHasMoreMessages() {
       return hasMoreMessages;
   }

   // 設定是否還有更多留言
   public void setHasMoreMessages(boolean hasMoreMessages) {
       this.hasMoreMessages = hasMoreMessages;
   }

   // 取得總留言數
   public int getTotalMessages() {
       return totalMessages;
   }

   // 設定總留言數
   public void setTotalMessages(int totalMessages) {
       this.totalMessages = totalMessages;
   }
}