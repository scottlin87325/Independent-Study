package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)   // 設定HTTP回應狀態為404
 // 貼文未找到異常的建構函數
public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
    // 使用貼文ID的建構函數
    public PostNotFoundException(Integer postId) {
        super("Post not found with id: " + postId);
    }
}