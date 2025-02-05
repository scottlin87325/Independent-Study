package com.scott.chat.config.commons;

import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;
import java.util.logging.Level;

// 自定義的檔案上傳解析器
// 主要用於處理多部分請求（例如文件上傳）
@Component
public class CustomMultipartResolver extends StandardServletMultipartResolver {
    
    private static final Logger logger = Logger.getLogger(CustomMultipartResolver.class.getName());
       
    // 覆寫判斷請求是否為多部分請求的方法
    // 添加日誌記錄功能，方便調試上傳問題 
    @Override
    public boolean isMultipart(HttpServletRequest request) {
        logger.fine("Checking if request is multipart: " + request.getContentType());
        return super.isMultipart(request);
    }
}