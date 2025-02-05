package com.scott.chat.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;

import com.scott.chat.model.Messagelog;
import com.scott.chat.model.MessageBoard; 
import com.scott.chat.model.Member;
import com.scott.chat.repository.MessagelogRepository;
import com.scott.chat.repository.MessageBoardRepository;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.dto.message.MessageDTO;
import com.scott.chat.exception.MessageNotFoundException;
import com.scott.chat.exception.UnauthorizedException;
import com.scott.chat.util.TimeUtils;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;

/**
* 訊息服務類
* 處理訊息的創建、查詢、更新和刪除等操作
*/
@Service
public class MessageService {
   
   // 日誌記錄器
   private static final Logger logger = Logger.getLogger(MessageService.class.getName());

   // 注入相依服務
   private final MessagelogRepository messagelogRepository;      // 訊息日誌資料存取
   private final MessageBoardRepository messageBoardRepository;  // 訊息看板資料存取
   private final MemberRepository memberRepository;             // 會員資料存取
   private final TimeUtils timeUtils;                          // 時間工具類

   /**
    * 建構子
    */
   @Autowired
   public MessageService(
       MessagelogRepository messagelogRepository,
       MessageBoardRepository messageBoardRepository,
       MemberRepository memberRepository,
       TimeUtils timeUtils
   ) {
       this.messagelogRepository = messagelogRepository;
       this.messageBoardRepository = messageBoardRepository;
       this.memberRepository = memberRepository;
       this.timeUtils = timeUtils;
   }

   /**
    * 建立新訊息
    * @param postId 貼文ID
    * @param message 訊息內容
    * @param file 附加檔案
    * @param currentUserId 當前用戶ID
    * @return 訊息DTO
    */
   @Transactional
   public MessageDTO createMessage(Integer postId, String message, MultipartFile file, Integer currentUserId) {
       logger.fine("正在為貼文 " + postId + " 創建訊息，用戶: " + currentUserId);
       
       // 檢查用戶是否存在
       Member currentUser = memberRepository.findById(currentUserId)
           .orElseThrow(() -> new UnauthorizedException("找不到用戶"));

       // 建立訊息日誌
       Messagelog messagelog = new Messagelog();
       messagelog.setMessage(message);
       messagelog.setMessagetime(timeUtils.getCurrentTimeString());

       // 處理附加檔案
       if (file != null && !file.isEmpty()) {
           messagelog.setMessagefilefile(file);
       }

       // 儲存訊息
       Messagelog savedMessage = messagelogRepository.save(messagelog);

       // 關聯訊息看板
       MessageBoard messageBoard = getOrCreateMessageBoard(postId);
       messageBoard.setMessagelogid(savedMessage.getMessageid());
       messageBoardRepository.save(messageBoard);

       return convertToDTO(savedMessage, currentUserId);
   }

   /**
    * 取得貼文的所有訊息
    * @param postId 貼文ID
    * @param pageRequest 分頁請求
    * @return 訊息DTO列表
    */
   @Transactional(readOnly = true)
   public List<MessageDTO> getMessagesByPost(Integer postId, PageRequest pageRequest) {
       List<Messagelog> messages = messagelogRepository.findByPostId(postId, pageRequest);
       return messages.stream()
           .map(message -> convertToDTO(message, message.getMessageid()))
           .collect(Collectors.toList());
   }

   /**
    * 刪除訊息
    * @param messageId 訊息ID
    * @param currentUserId 當前用戶ID
    * @throws UnauthorizedException 若用戶無權限刪除
    */
   @Transactional
   public void deleteMessage(Integer messageId, Integer currentUserId) {
       Messagelog message = messagelogRepository.findById(messageId)
           .orElseThrow(() -> new MessageNotFoundException(messageId));

       if (!isMessageOwner(message, currentUserId)) {
           throw new UnauthorizedException("您沒有權限刪除此訊息");
       }

       messagelogRepository.delete(message);
   }

   /**
    * 更新訊息內容
    * @param messageId 訊息ID
    * @param updatedMessage 更新的內容
    * @param currentUserId 當前用戶ID
    * @return 更新後的訊息DTO
    */
   @Transactional
   public MessageDTO updateMessage(Integer messageId, String updatedMessage, Integer currentUserId) {
       Messagelog message = messagelogRepository.findById(messageId)
           .orElseThrow(() -> new MessageNotFoundException(messageId));

       if (!isMessageOwner(message, currentUserId)) {
           throw new UnauthorizedException("您沒有權限更新此訊息");
       }

       message.setMessage(updatedMessage);
       Messagelog updatedMsg = messagelogRepository.save(message);
       
       return convertToDTO(updatedMsg, currentUserId);
   }

   /**
    * 將訊息實體轉換為DTO
    */
   private MessageDTO convertToDTO(Messagelog messagelog, Integer currentUserId) {
       Member author = memberRepository.findById(currentUserId)
           .orElseThrow(() -> new RuntimeException("找不到訊息作者"));

       boolean isCurrentUser = author != null && author.getMemberid().equals(currentUserId);

       MessageDTO dto = new MessageDTO();
       dto.setMessageId(messagelog.getMessageid());
       dto.setMessage(messagelog.getMessage());
       dto.setMessageTime(messagelog.getMessagetime());
       dto.setMessageFileUrl(messagelog.getMessagefilebase64());
       dto.setUserId(author.getMemberid());
       dto.setUserName(author.getMembername());
       dto.setUserAvatar(author.getMemberphoto() != null ? 
           Base64.getEncoder().encodeToString(author.getMemberphoto()) : null);
       dto.setCurrentUser(isCurrentUser);
       dto.setCanEdit(isCurrentUser);
       dto.setCanDelete(isCurrentUser);
       
       return dto;
   }

   /**
    * 檢查使用者是否為訊息擁有者
    */
   private boolean isMessageOwner(Messagelog message, Integer userId) {
       return message.getMessageid().equals(userId);
   }

   /**
    * 獲取或創建訊息看板
    */
   private MessageBoard getOrCreateMessageBoard(Integer postId) {
       return messageBoardRepository.findByPostid(postId)
           .orElseGet(() -> {
               MessageBoard newBoard = new MessageBoard();
               newBoard.setPostid(postId);
               newBoard.setMessagelikedcount(0);
               return messageBoardRepository.save(newBoard);
           });
   }
}