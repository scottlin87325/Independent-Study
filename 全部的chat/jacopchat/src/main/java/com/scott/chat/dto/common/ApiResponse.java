package com.scott.chat.dto.common;

public class ApiResponse<T> {
    private int status;          // HTTP狀態碼
    private String message;      // 回應訊息
    private T data;             // 回應資料
    
    // 無參數建構子
    public ApiResponse() {
    }
    
    // 全參數建構子
    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    
    // Getters
    public int getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public T getData() {
        return data;
    }
    
    // Setters
    public void setStatus(int status) {
        this.status = status;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    // Builder 類
    public static class Builder<T> {
        private int status;
        private String message;
        private T data;
        
        public Builder<T> status(int status) {
            this.status = status;
            return this;
        }
        
        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }
        
        public ApiResponse<T> build() {
            return new ApiResponse<>(status, message, data);
        }
    }
    
    // Builder 方法
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    // 靜態工廠方法
    public static <T> ApiResponse<T> success(T data) {
        return new Builder<T>()
                .status(200)
                .message("success")
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new Builder<T>()
                .status(400)
                .message(message)
                .build();
    }
}