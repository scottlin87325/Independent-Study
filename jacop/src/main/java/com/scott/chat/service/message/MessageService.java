package com.scott.chat.service.message;

// 導入必要的依賴
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

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;

// 訊息服務類，處理所有與訊息相關的業務邏輯
@Service
@Slf4j  // 啟用日誌功能
public class MessageService {

    // 注入所需的資源庫
    private final MessagelogRepository messagelogRepository;
    private final MessageBoardRepository messageBoardRepository;
    private final MemberRepository memberRepository;
    private final TimeUtils timeUtils;

    // 構造函數，注入依賴
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

    // 創建新訊息的方法
    @Transactional
    public MessageDTO createMessage(Integer postId, String message, MultipartFile file, Integer currentUserId) {
        log.debug("Creating message for post {} by user {}", postId, currentUserId);
        
        // 檢查用戶是否存在
        Member currentUser = memberRepository.findById(currentUserId)
            .orElseThrow(() -> new UnauthorizedException("User not found"));

        // 創建新的訊息記錄
        Messagelog messagelog = new Messagelog();
        messagelog.setMessage(message);
        messagelog.setMessagetime(timeUtils.getCurrentTimeString());

        // 如果有上傳文件，保存文件
        if (file != null && !file.isEmpty()) {
            messagelog.setMessagefilefile(file);
        }

        // 保存訊息到數據庫
        Messagelog savedMessage = messagelogRepository.save(messagelog);

        // 更新訊息板
        MessageBoard messageBoard = getOrCreateMessageBoard(postId);
        messageBoard.setMessagelogid(savedMessage.getMessageid());
        messageBoardRepository.save(messageBoard);

        return convertToDTO(savedMessage, currentUserId);
    }

    // 獲取特定貼文的訊息列表，支持分頁
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessagesByPost(Integer postId, PageRequest pageRequest) {
        List<Messagelog> messages = messagelogRepository.findByPostId(postId, pageRequest);
        return messages.stream()
            .map(message -> convertToDTO(message, message.getMessageid()))
            .collect(Collectors.toList());
    }

    // 刪除訊息的方法
    @Transactional
    public void deleteMessage(Integer messageId, Integer currentUserId) {
        // 檢查訊息是否存在
        Messagelog message = messagelogRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));

        // 檢查是否有權限刪除
        if (!isMessageOwner(message, currentUserId)) {
            throw new UnauthorizedException("You don't have permission to delete this message");
        }

        messagelogRepository.delete(message);
    }

    // 更新訊息內容的方法
    @Transactional
    public MessageDTO updateMessage(Integer messageId, String updatedMessage, Integer currentUserId) {
        // 檢查訊息是否存在
        Messagelog message = messagelogRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));

        // 檢查是否有權限更新
        if (!isMessageOwner(message, currentUserId)) {
            throw new UnauthorizedException("You don't have permission to update this message");
        }

        message.setMessage(updatedMessage);
        Messagelog updatedMsg = messagelogRepository.save(message);
        
        return convertToDTO(updatedMsg, currentUserId);
    }

    // 將訊息實體轉換為DTO對象的方法
    private MessageDTO convertToDTO(Messagelog messagelog, Integer currentUserId) {
        // 獲取作者信息
        Member author = memberRepository.findById(currentUserId)
            .orElseThrow(() -> new RuntimeException("Message author not found"));

        boolean isCurrentUser = author != null && author.getMemberid().equals(currentUserId);

        // 構建DTO對象
        return MessageDTO.builder()
            .messageId(messagelog.getMessageid())
            .message(messagelog.getMessage())
            .messageTime(messagelog.getMessagetime())
            .messageFileUrl(messagelog.getMessagefilebase64())
            .userId(author.getMemberid())
            .userName(author.getMembername())
            .userAvatar(author.getMemberphoto() != null ? 
                Base64.getEncoder().encodeToString(author.getMemberphoto()) : null)
            .isCurrentUser(isCurrentUser)
            .canEdit(isCurrentUser)
            .canDelete(isCurrentUser)
            .build();
    }

    // 檢查是否為訊息擁有者的方法
    private boolean isMessageOwner(Messagelog message, Integer userId) {
        return message.getMessageid().equals(userId);
    }

    // 獲取或創建訊息板的方法
    private MessageBoard getOrCreateMessageBoard(Integer postId) {
        return messageBoardRepository.findByPostid(postId)
            .orElseGet(() -> {
                // 如果找不到就創建新的訊息板
                MessageBoard newBoard = new MessageBoard();
                newBoard.setPostid(postId);
                newBoard.setMessagelikedcount(0);
                return messageBoardRepository.save(newBoard);
            });
    }
}