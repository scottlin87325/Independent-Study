package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 設定HTTP回應狀態為400
public class FileUploadException extends RuntimeException {
    // 檔案上傳異常的建構函數
    public FileUploadException(String message) {
        super(message);
    }
     // 帶有原因的檔案上傳異常建構函數
    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}