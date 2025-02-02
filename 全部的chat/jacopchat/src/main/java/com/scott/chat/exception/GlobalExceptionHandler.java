package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.scott.chat.dto.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
// 全局異常處理器：用於統一處理所有控制器拋出的異常
@RestControllerAdvice  // 標記此類為REST API的全局異常處理器
@Slf4j  // 啟用日誌功能
public class GlobalExceptionHandler {

    // 處理找不到貼文的異常
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFound(PostNotFoundException ex) {
        log.error("Post not found", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .status(404)
                        .message(ex.getMessage())
                        .build());
    }

    // 處理找不到訊息的異常
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotFound(MessageNotFoundException ex) {
        log.error("Message not found", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .status(404)
                        .message(ex.getMessage())
                        .build());
    }

    // 處理文件上傳失敗的異常
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileUpload(FileUploadException ex) {
        log.error("File upload error", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .status(400)
                        .message(ex.getMessage())
                        .build());
    }

    // 處理上傳文件超過大小限制的異常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        log.error("File size exceeded", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .status(400)
                        .message("File size exceeded the maximum limit")
                        .build());
    }

    // 處理無效請求的異常
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequest(InvalidRequestException ex) {
        log.error("Invalid request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .status(400)
                        .message(ex.getMessage())
                        .build());
    }

    // 處理所有未被特定處理器捕獲的異常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .status(500)
                        .message("An unexpected error occurred")
                        .build());
    }

    // 處理未授權訪問的異常
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
        log.error("Unauthorized access", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.<Void>builder()
                        .status(403)
                        .message(ex.getMessage())
                        .build());
    }
}