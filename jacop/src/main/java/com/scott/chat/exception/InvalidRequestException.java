package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 設定HTTP回應狀態為400
public class InvalidRequestException extends RuntimeException {
    // 無效請求異常的建構函數
    public InvalidRequestException(String message) {
        super(message);
    }
}