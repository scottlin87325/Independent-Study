package com.scott.chat.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scott.chat.model.Post;
import com.scott.chat.repository.SearchRepository;

@Service
public class LikeService {
    private static final Logger logger = Logger.getLogger(LikeService.class.getName());
    
    @Autowired
    private SearchRepository searchRepository;
    
    @Transactional
    public Integer incrementLikeCount(Integer postId) {
        try {
            logger.info("處理按讚請求 - 貼文ID: " + postId);
            
            // 使用 findById 並立即檢查是否存在
            Post post = searchRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("找不到指定的貼文，ID: " + postId));
            
            // 確保當前按讚數不為 null
            Integer currentLikes = post.getLikedcount();
            if (currentLikes == null) {
                currentLikes = 0;
            }
            
            // 更新按讚數
            post.setLikedcount(currentLikes + 1);
            
            // 儲存並取得更新後的實體
            Post savedPost = searchRepository.save(post);
            
            // 確保返回值不為 null
            Integer newLikeCount = savedPost.getLikedcount();
            if (newLikeCount == null) {
                newLikeCount = currentLikes + 1;
            }
            
            logger.info("按讚成功 - 貼文ID: " + postId + ", 新的按讚數: " + newLikeCount);
            
            return newLikeCount;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "處理按讚請求時發生錯誤 - 貼文ID: " + postId, e);
            throw new RuntimeException("處理按讚請求失敗", e);
        }
    }
}
