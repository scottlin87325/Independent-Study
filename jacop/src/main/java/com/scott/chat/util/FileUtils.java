package com.scott.chat.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

@Component
public class FileUtils {
   
   private static final Logger logger = Logger.getLogger(FileUtils.class.getName());
   
   private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
   private static final String[] ALLOWED_IMAGE_TYPES = {
       "image/jpeg", 
       "image/png", 
       "image/gif",
       "image/webp"
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
           logger.warning("File too large: " + file.getSize() + " bytes");
           return false;
       }

       // 檢查檔案類型
       String contentType = file.getContentType();
       if (contentType == null) {
           return false;
       }

       // 對於 GIF 檔案，只驗證文件類型
       if (contentType.equals("image/gif")) {
           return true;
       }

       // 對於其他圖片類型，執行額外驗證
       for (String allowedType : ALLOWED_IMAGE_TYPES) {
           if (contentType.equals(allowedType)) {
               return validateStaticImage(file);
           }
       }
       
       logger.warning("Invalid file type: " + contentType);
       return false;
   }

   private boolean validateStaticImage(MultipartFile file) {
       try {
           BufferedImage image = ImageIO.read(file.getInputStream());
           return image != null;
       } catch (IOException e) {
           logger.log(Level.SEVERE, "Error validating static image", e);
           return false;
       }
   }

   /**
    * 將MultipartFile轉換為byte數組
    */
   public byte[] convertMultipartFileToBytes(MultipartFile file) throws IOException {
       String contentType = file.getContentType();
       // 對於 GIF 檔案，直接返回原始數據
       if (contentType != null && contentType.equals("image/gif")) {
           return file.getBytes();
       }

       try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
           file.getInputStream().transferTo(baos);
           return baos.toByteArray();
       } catch (IOException e) {
           logger.log(Level.SEVERE, "Error converting MultipartFile to bytes", e);
           throw e;
       }
   }

   /**
    * 驗證圖片的尺寸
    */
   public boolean isValidImageDimensions(byte[] imageBytes, int maxWidth, int maxHeight) {
       // 尝试读取为 BufferedImage
       try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
           BufferedImage image = ImageIO.read(bis);
           // 如果无法读取（例如是 GIF），则跳过尺寸验证
           if (image == null) {
               logger.fine("Skipping dimension validation for non-static image");
               return true;
           }
           return image.getWidth() <= maxWidth && image.getHeight() <= maxHeight;
       } catch (IOException e) {
           logger.log(Level.SEVERE, "Error validating image dimensions", e);
           // 如果验证失败，也允许通过
           return true;
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
           logger.log(Level.SEVERE, "Error checking if file is image", e);
           return false;
       }
   }
}