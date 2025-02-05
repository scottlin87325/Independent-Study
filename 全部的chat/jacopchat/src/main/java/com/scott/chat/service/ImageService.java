package com.scott.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

/**
* 圖片處理服務
* 負責圖片的上傳、裁切、調整大小、儲存和刪除等操作
*/
@Service
public class ImageService {
   
   // 日誌記錄器
   private static final Logger logger = Logger.getLogger(ImageService.class.getName());
   
   // 圖片上傳路徑，從配置文件讀取
   @Value("${upload.path}")
   private String uploadPath;
   
   // 上傳根目錄路徑
   private final Path root;
   
   /**
    * 建構函數
    * @param uploadPath 上傳路徑
    */
   public ImageService(@Value("${upload.path}") String uploadPath) {
       this.root = Paths.get(uploadPath);
       init();
   }
   
   /**
    * 初始化上傳目錄
    */
   public void init() {
       try {
           Files.createDirectories(root);
       } catch (IOException e) {
           logger.log(Level.SEVERE, "無法初始化上傳資料夾!");
           throw new RuntimeException("無法初始化上傳資料夾!");
       }
   }
   
   /**
    * 裁切並調整圖片大小
    * @param file 原始圖片
    * @param x 裁切起點X座標
    * @param y 裁切起點Y座標
    * @param width 裁切寬度
    * @param height 裁切高度
    * @return 處理後的圖片位元組資料
    */
   public byte[] cropAndResizeImage(MultipartFile file, int x, int y, int width, int height) throws IOException {
       try {
           BufferedImage originalImage = ImageIO.read(file.getInputStream());
           
           // 確保裁切區域不超出原圖範圍
           x = Math.max(0, Math.min(x, originalImage.getWidth()));
           y = Math.max(0, Math.min(y, originalImage.getHeight()));
           width = Math.min(width, originalImage.getWidth() - x);
           height = Math.min(height, originalImage.getHeight() - y);
           
           validateCropParameters(originalImage, x, y, width, height);
           
           // 使用 Thumbnails 進行裁切和調整大小
           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           Thumbnails.of(originalImage)
               .sourceRegion(x, y, width, height)
               .size(width, height)
               .outputQuality(0.85)
               .outputFormat("jpg")
               .toOutputStream(outputStream);
               
           return outputStream.toByteArray();
       } catch (Exception e) {
           logger.log(Level.SEVERE, "圖片處理失敗", e);
           throw new IOException("圖片處理失敗: " + e.getMessage(), e);
       }
   }
   
   /**
    * 儲存圖片
    * @param imageData 圖片位元組資料
    * @param originalFilename 原始檔名
    * @return 儲存後的新檔名
    */
   public String saveImage(byte[] imageData, String originalFilename) {
       try {
           String filename = generateUniqueFilename(originalFilename);
           Path targetLocation = this.root.resolve(filename);
           
           try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
               Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
           }
           
           return filename;
       } catch (IOException e) {
           logger.log(Level.SEVERE, "儲存圖片失敗", e);
           throw new RuntimeException("儲存圖片失敗", e);
       }
   }
   
   /**
    * 載入圖片
    * @param filename 檔名
    * @return 圖片資源
    */
   public Resource loadImage(String filename) {
       try {
           Path file = root.resolve(filename);
           Resource resource = new UrlResource(file.toUri());
           
           if (resource.exists() || resource.isReadable()) {
               return resource;
           } else {
               throw new RuntimeException("無法讀取檔案!");
           }
       } catch (IOException e) {
           throw new RuntimeException("錯誤: " + e.getMessage());
       }
   }
   
   /**
    * 刪除圖片
    * @param filename 要刪除的檔名
    */
   public void deleteImage(String filename) {
       try {
           Path file = root.resolve(filename);
           Files.deleteIfExists(file);
       } catch (IOException e) {
           logger.log(Level.SEVERE, "刪除圖片失敗", e);
           throw new RuntimeException("刪除圖片失敗", e);
       }
   }
   
   /**
    * 載入所有圖片
    * @return 圖片路徑串流
    */
   public Stream<Path> loadAll() {
       try {
           return Files.walk(this.root, 1)
               .filter(path -> !path.equals(this.root))
               .map(this.root::relativize);
       } catch (IOException e) {
           throw new RuntimeException("無法載入檔案!");
       }
   }
   
   /**
    * 生成唯一檔名
    * @param originalFilename 原始檔名
    * @return 新的唯一檔名
    */
   private String generateUniqueFilename(String originalFilename) {
       String extension = getImageExtension(originalFilename);
       return UUID.randomUUID().toString() + "." + extension;
   }
   
   /**
    * 取得圖片副檔名
    * @param filename 檔名
    * @return 副檔名
    */
   private String getImageExtension(String filename) {
       return StringUtils.getFilenameExtension(filename) != null ? 
           StringUtils.getFilenameExtension(filename).toLowerCase() : "jpg";
   }
   
   /**
    * 驗證裁切參數是否有效
    * @param image 原始圖片
    * @param x 裁切X座標
    * @param y 裁切Y座標
    * @param width 裁切寬度
    * @param height 裁切高度
    */
   private void validateCropParameters(BufferedImage image, int x, int y, int width, int height) {
       if (x < 0 || y < 0 || width <= 0 || height <= 0) {
           throw new IllegalArgumentException("無效的裁切參數: 數值必須為正數");
       }
       
       if (x + width > image.getWidth() || y + height > image.getHeight()) {
           throw new IllegalArgumentException(
               String.format("裁切區域 (%d,%d,%d,%d) 超出圖片範圍 (%d,%d)",
                   x, y, width, height, image.getWidth(), image.getHeight())
           );
       }
   }
   
   /**
    * 刪除所有圖片
    */
   public void deleteAll() {
       FileSystemUtils.deleteRecursively(root.toFile());
   }
   
   /**
    * 檢查檔案是否為圖片
    * @param file 要檢查的檔案
    * @return 是否為圖片
    */
   public boolean isImageFile(MultipartFile file) {
       String contentType = file.getContentType();
       return contentType != null && contentType.startsWith("image/");
   }
   
   /**
    * 調整圖片尺寸
    * @param originalImage 原始圖片
    * @param targetWidth 目標寬度
    * @return 調整後的圖片
    */
   public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
       double ratio = (double) targetWidth / originalImage.getWidth();
       int targetHeight = (int) (originalImage.getHeight() * ratio);
       
       return Thumbnails.of(originalImage)
           .size(targetWidth, targetHeight)
           .asBufferedImage();
   }
}