package com.scott.chat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Collection;
import java.util.regex.Pattern;

@Component
public class ValidationUtils {

    private static final Logger logger = Logger.getLogger(ValidationUtils.class.getName());

    private final FileUtils fileUtils;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    
    @Autowired
    public ValidationUtils(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    // 貼文內容驗證
    public boolean isValidPostContent(String content) {
        if (!StringUtils.hasText(content)) {
            logger.warning("Post content is empty");
            return false;
        }
        if (content.length() > 1000) {
            logger.warning("Post content exceeds 1000 characters");
            return false;
        }
        return true;
    }

    // 貼文圖片驗證
    public boolean isValidPhoto(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            logger.warning("Photo is null or empty");
            return false;
        }

        // 檢查檔案大小
        if (photo.getSize() > MAX_FILE_SIZE) {
            logger.warning("File size exceeds limit: " + photo.getSize());
            return false;
        }

        String contentType = photo.getContentType();
        // GIF 文件特殊处理
        if (contentType != null && contentType.equals("image/gif")) {
            // GIF 文件只需檢查檔案類型和大小
            return true;
        }

        // 其他图片类型使用常规验证
        boolean isValid = fileUtils.isValidImageFile(photo);
        if (!isValid) {
            logger.warning("Invalid image file: " + photo.getOriginalFilename() + ", type: " + contentType);
        }
        return isValid;
    }

    // 圖片集合驗證
    public boolean isValidPhotoList(Collection<MultipartFile> photos) {
        if (photos == null || photos.isEmpty()) {
            logger.warning("Photo list is null or empty");
            return false;
        }
        if (photos.size() > 10) {
            logger.warning("Too many photos: " + photos.size());
            return false;
        }
        return photos.stream().allMatch(this::isValidPhoto);
    }

    // 用戶ID驗證
    public boolean isValidUserId(Integer userId) {
        if (userId == null || userId <= 0) {
            logger.warning("Invalid user ID: " + userId);
            return false;
        }
        return true;
    }

    // 留言內容驗證
    public boolean isValidMessage(String message) {
        if (!StringUtils.hasText(message)) {
            logger.warning("Message is empty");
            return false;
        }
        if (message.length() > 500) {
            logger.warning("Message exceeds 500 characters");
            return false;
        }
        return true;
    }

    // Email格式驗證
    public boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex)
                     .matcher(email)
                     .matches();
    }

    // 密碼強度驗證
    public boolean isValidPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        // 至少8位，包含大小寫字母和數字
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        return Pattern.compile(passwordRegex)
                     .matcher(password)
                     .matches();
    }

    // 用戶名驗證
    public boolean isValidUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        if (username.length() < 3 || username.length() > 20) {
            return false;
        }
        // 只允許字母、數字和底線
        String usernameRegex = "^[a-zA-Z0-9_]{3,20}$";
        return Pattern.compile(usernameRegex)
                     .matcher(username)
                     .matches();
    }

    // XSS 防護
    public String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // 基本的 XSS 過濾
        return input.replaceAll("<", "&lt;")
                   .replaceAll(">", "&gt;")
                   .replaceAll("\"", "&quot;")
                   .replaceAll("'", "&#x27;")
                   .replaceAll("&", "&amp;");
    }

    // SQL 注入防護
    public boolean containsSqlInjection(String input) {
        if (input == null) {
            return false;
        }
        String sqlCheckRegex = "(?i)(select|insert|update|delete|drop|union|exec|declare|truncate)";
        return Pattern.compile(sqlCheckRegex).matcher(input).find();
    }
}