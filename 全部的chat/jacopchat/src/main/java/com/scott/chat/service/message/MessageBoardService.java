package com.scott.chat.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scott.chat.model.MessageBoard;
import com.scott.chat.model.Messagelog;
import com.scott.chat.repository.MessageBoardRepository;
import com.scott.chat.repository.MessagelogRepository;
import com.scott.chat.service.post.PostService;

import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

@Service
@Slf4j
public class MessageBoardService {
    
    private final MessageBoardRepository messageBoardRepository;
    private final MessagelogRepository messagelogRepository;
    private final PostService postService;

    @Autowired
    public MessageBoardService(
        MessageBoardRepository messageBoardRepository,
        MessagelogRepository messagelogRepository,
        PostService postService
    ) {
        this.messageBoardRepository = messageBoardRepository;
        this.messagelogRepository = messagelogRepository;
        this.postService = postService;
    }

    // 創建留言板
    @Transactional
    public MessageBoard createMessageBoard(Integer postId) {
        // 檢查是否已存在
        Optional<MessageBoard> existingBoard = messageBoardRepository.findByPostid(postId);
        if (existingBoard.isPresent()) {
            return existingBoard.get();
        }

        MessageBoard newBoard = new MessageBoard();
        newBoard.setPostid(postId);
        newBoard.setMessagelikedcount(0);
        return messageBoardRepository.save(newBoard);
    }

    // 獲取留言板
    public MessageBoard getMessageBoard(Integer postId) {
        return messageBoardRepository.findByPostid(postId)
            .orElseThrow(() -> new RuntimeException("Message board not found for post: " + postId));
    }

    // 更新留言按讚數
    @Transactional
    public void updateLikeCount(Integer messageBoardId, boolean isIncrease) {
        MessageBoard board = messageBoardRepository.findById(messageBoardId)
            .orElseThrow(() -> new RuntimeException("Message board not found"));
        
        int currentCount = board.getMessagelikedcount();
        if (isIncrease) {
            board.setMessagelikedcount(currentCount + 1);
        } else {
            board.setMessagelikedcount(Math.max(0, currentCount - 1));
        }
        
        messageBoardRepository.save(board);
    }

    // 添加留言到留言板
    @Transactional
    public void addMessageToBoard(Integer postId, Messagelog message) {
        MessageBoard board = getOrCreateMessageBoard(postId);
        board.setMessagelogid(message.getMessageid());
        messageBoardRepository.save(board);
        
        // 更新貼文的留言計數
        postService.updateMessageCount(postId, true);
    }

    // 從留言板刪除留言
    @Transactional
    public void removeMessageFromBoard(Integer postId, Integer messageId) {
        MessageBoard board = getMessageBoard(postId);
        if (board.getMessagelogid().equals(messageId)) {
            board.setMessagelogid(null);
            messageBoardRepository.save(board);
            
            // 更新貼文的留言計數
            postService.updateMessageCount(postId, false);
        }
    }

    // 獲取或創建留言板
    private MessageBoard getOrCreateMessageBoard(Integer postId) {
        return messageBoardRepository.findByPostid(postId)
            .orElseGet(() -> createMessageBoard(postId));
    }

    // 檢查留言是否存在於留言板
    public boolean isMessageExistsInBoard(Integer postId, Integer messageId) {
        Optional<MessageBoard> board = messageBoardRepository.findByPostid(postId);
        return board.map(b -> messageId.equals(b.getMessagelogid())).orElse(false);
    }

    // 清空留言板
    @Transactional
    public void clearMessageBoard(Integer postId) {
        MessageBoard board = getMessageBoard(postId);
        board.setMessagelogid(null);
        board.setMessagelikedcount(0);
        messageBoardRepository.save(board);
    }
}