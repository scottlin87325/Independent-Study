package com.scott.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import com.scott.chat.dto.common.ApiResponse;
import java.util.logging.Logger;
import java.util.logging.Level;

// 全局異常處理器：用於統一處理所有控制器拋出的異常
@RestControllerAdvice  // 標記此類為REST API的全局異常處理器
public class GlobalExceptionHandler {
    
    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());
    
    // 處理找不到貼文的異常
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handlePostNotFound(PostNotFoundException ex) {
        logger.log(Level.SEVERE, "Post not found", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(404);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    // 處理找不到訊息的異常
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleMessageNotFound(MessageNotFoundException ex) {
        logger.log(Level.SEVERE, "Message not found", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(404);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    // 處理文件上傳失敗的異常
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileUpload(FileUploadException ex) {
        logger.log(Level.SEVERE, "File upload error", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    // 處理上傳文件超過大小限制的異常
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        logger.log(Level.SEVERE, "File size exceeded", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMessage("File size exceeded the maximum limit");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    // 處理無效請求的異常
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequest(InvalidRequestException ex) {
        logger.log(Level.SEVERE, "Invalid request", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    // 處理所有未被特定處理器捕獲的異常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex) {
        logger.log(Level.SEVERE, "Unexpected error", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(500);
        response.setMessage("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    // 處理未授權訪問的異常
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
        logger.log(Level.SEVERE, "Unauthorized access", ex);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus(403);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}