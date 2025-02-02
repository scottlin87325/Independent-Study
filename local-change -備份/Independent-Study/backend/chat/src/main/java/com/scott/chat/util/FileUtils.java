package com.scott.chat.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUtils {
   
   private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
   private static final String[] ALLOWED_IMAGE_TYPES = {
       "image/jpeg", 
       "image/png", 
       "image/gif"
   };

   /**
    * 驗證檔案是否為合法的圖片
    */
   public boolean isValidImageFile(MultipartFile file) {
       if (file == null || file.isEmpty()) {
           return false;
       }

       // 檢查檔案大小
       if (file.getSize() > MAX_FILE_SIZE) {
           log.warn("File too large: {} bytes", file.getSize());
           return false;
       }

       // 檢查檔案類型
       String contentType = file.getContentType();
       if (contentType == null) {
           return false;
       }

       for (String allowedType : ALLOWED_IMAGE_TYPES) {
           if (contentType.equals(allowedType)) {
               return true;
           }
       }
       
       log.warn("Invalid file type: {}", contentType);
       return false;
   }

   /**
    * 將MultipartFile轉換為byte數組
    */
   public byte[] convertMultipartFileToBytes(MultipartFile file) throws IOException {
       try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
           file.getInputStream().transferTo(baos);
           return baos.toByteArray();
       } catch (IOException e) {
           log.error("Error converting MultipartFile to bytes", e);
           throw e;
       }
   }

   /**
    * 將byte數組轉換為Base64字符串
    */
   public String convertBytesToBase64(byte[] bytes) {
       return Base64.getEncoder().encodeToString(bytes);
   }

   /**
    * 驗證圖片的尺寸
    */
   public boolean isValidImageDimensions(byte[] imageBytes, int maxWidth, int maxHeight) {
       try {
           BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
           if (image == null) {
               return false;
           }
           return image.getWidth() <= maxWidth && image.getHeight() <= maxHeight;
       } catch (IOException e) {
           log.error("Error validating image dimensions", e);
           return false;
       }
   }

   /**
    * 獲取檔案大小（MB）
    */
   public double getFileSizeInMB(MultipartFile file) {
       return file.getSize() / (1024.0 * 1024.0);
   }

   /**
    * 獲取檔案副檔名
    */
   public String getFileExtension(String fileName) {
       if (fileName == null || fileName.lastIndexOf(".") == -1) {
           return "";
       }
       return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
   }

   /**
    * 生成唯一的檔案名
    */
   public String generateUniqueFileName(String originalFileName) {
       String extension = getFileExtension(originalFileName);
       return System.currentTimeMillis() + "_" + 
              Math.abs(originalFileName.hashCode()) + 
              (extension.isEmpty() ? "" : "." + extension);
   }

   /**
    * 驗證檔案內容類型
    */
   public boolean isValidContentType(String contentType, String[] allowedTypes) {
       if (contentType == null || allowedTypes == null) {
           return false;
       }
       for (String allowedType : allowedTypes) {
           if (contentType.equals(allowedType)) {
               return true;
           }
       }
       return false;
   }

   /**
    * 檢查檔案是否為圖片
    */
   public boolean isImageFile(MultipartFile file) {
       if (file == null || file.isEmpty()) {
           return false;
       }
       try {
           BufferedImage image = ImageIO.read(file.getInputStream());
           return image != null;
       } catch (IOException e) {
           log.error("Error checking if file is image", e);
           return false;
       }
   }
}