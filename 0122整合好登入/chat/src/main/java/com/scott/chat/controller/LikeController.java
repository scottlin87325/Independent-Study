package com.scott.chat.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scott.chat.service.LikeService;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    private static final Logger logger = Logger.getLogger(LikeController.class.getName());
    
    @Autowired
    private LikeService likeService;
    
    @PutMapping("/{postId}")
    public ResponseEntity<Integer> updateLikeCount(@PathVariable Integer postId) {
        try {
            logger.info("接收按讚請求 - 貼文ID: " + postId);
            Integer newLikeCount = likeService.incrementLikeCount(postId);
            logger.info("按讚請求處理完成 - 貼文ID: " + postId + ", 新的按讚數: " + newLikeCount);
            return ResponseEntity.ok(newLikeCount);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "處理按讚請求時發生錯誤 - 貼文ID: " + postId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}