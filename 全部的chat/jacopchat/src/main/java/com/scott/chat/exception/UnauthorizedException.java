package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 設定HTTP回應狀態為403
public class UnauthorizedException extends RuntimeException {
    // 未授權異常的建構函數
    public UnauthorizedException(String message) {
        super(message);
    }
     // 帶有原因的未授權異常建構函數
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}