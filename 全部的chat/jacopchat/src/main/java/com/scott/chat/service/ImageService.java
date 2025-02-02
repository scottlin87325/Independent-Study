package com.scott.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${upload.path}")
    private String uploadPath;
    
    public String uploadImage(MultipartFile file) {
        try {
            // 生成唯一的檔案名
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            
            // 壓縮並調整圖片大小
            BufferedImage resizedImage = Thumbnails.of(file.getInputStream())
                .size(1080, 1080)
                .outputQuality(0.8)
                .asBufferedImage();
            
            // 確保上傳目錄存在
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 儲存壓縮後的圖片
            File outputFile = new File(uploadPath, fileName);
            ImageIO.write(resizedImage, getFileExtension(fileName).substring(1), outputFile);
            
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("無法處理圖片上傳: " + e.getMessage(), e);
        }
    }
    
    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID().toString() + getFileExtension(originalFilename);
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return ".jpg"; // 默認副檔名
    }
}