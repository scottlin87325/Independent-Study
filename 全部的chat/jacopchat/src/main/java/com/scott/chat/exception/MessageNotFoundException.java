package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 設定HTTP回應狀態為404
public class MessageNotFoundException extends RuntimeException {
    // 留言未找到異常的建構函數
    public MessageNotFoundException(String message) {
        super(message);
    }
    // 使用留言ID的建構函數
    public MessageNotFoundException(Integer messageId) {
        super("Message not found with id: " + messageId);
    }
}