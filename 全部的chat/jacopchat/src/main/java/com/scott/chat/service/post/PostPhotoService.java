package com.scott.chat.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.scott.chat.model.PostPhoto;
import com.scott.chat.repository.PostPhotoRepository;
import com.scott.chat.util.FileUtils;
import com.scott.chat.util.ValidationUtils;
import com.scott.chat.exception.FileUploadException;
import com.scott.chat.exception.InvalidRequestException;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 貼文圖片服務
 * 負責處理貼文相關的圖片上傳、存儲、讀取和刪除等操作
 * 包含圖片優化、大小驗證等功能
 */
@Service
public class PostPhotoService {
    
    // 日誌記錄器
    private static final Logger logger = Logger.getLogger(PostPhotoService.class.getName());
    
    // 注入相關依賴
    private final PostPhotoRepository postPhotoRepository;  // 圖片資料庫操作
    private final FileUtils fileUtils;                     // 檔案處理工具
    private final ValidationUtils validationUtils;         // 驗證工具
    
    // 從配置檔讀取上傳路徑
    @Value("${upload.path}")
    private String uploadPath;
    
    /**
     * 建構函數，注入必要的依賴
     * @param postPhotoRepository 圖片儲存庫
     * @param fileUtils 檔案工具
     * @param validationUtils 驗證工具
     */
    @Autowired
    public PostPhotoService(
        PostPhotoRepository postPhotoRepository,
        FileUtils fileUtils,
        ValidationUtils validationUtils
    ) {
        this.postPhotoRepository = postPhotoRepository;
        this.fileUtils = fileUtils;
        this.validationUtils = validationUtils;
    }
    
    /**
     * 儲存貼文圖片的簡化方法，預設不進行裁切
     * @param postId 貼文ID
     * @param photos 要儲存的圖片陣列
     * @return 已儲存的圖片列表
     */
    @Transactional
    public List<PostPhoto> savePhotos(Integer postId, MultipartFile[] photos) {
        return savePhotos(postId, photos, false);  // 預設為非裁切模式
    }

    /**
     * 儲存貼文圖片的完整方法
     * @param postId 貼文ID
     * @param photos 要儲存的圖片陣列
     * @param isCropped 是否已經過裁切處理
     * @return 已儲存的圖片列表
     * @throws InvalidRequestException 當圖片數量超過限制時
     * @throws FileUploadException 當圖片儲存失敗時
     */
    @Transactional
    public List<PostPhoto> savePhotos(Integer postId, MultipartFile[] photos, boolean isCropped) {
        logger.fine("正在儲存貼文 " + postId + " 的圖片，已裁切: " + isCropped);
        
        // 檢查圖片是否為空
        if (photos == null || photos.length == 0) {
            return Collections.emptyList();
        }
        
        // 檢查圖片數量限制
        if (photos.length > 10) {
            throw new InvalidRequestException("最多只能上傳10張照片");
        }
        
        List<PostPhoto> savedPhotos = new ArrayList<>();
        
        // 處理每張圖片
        for (MultipartFile photo : photos) {
            try {
                // 驗證圖片格式和大小
                if (!validationUtils.isValidPhoto(photo)) {
                    throw new FileUploadException("無效的圖片格式或大小: " + photo.getOriginalFilename());
                }
                
                // 根據是否需要裁切決定處理方式
                byte[] processedImage;
                if (!isCropped) {
                    processedImage = optimizeImage(photo);  // 優化圖片
                } else {
                    processedImage = photo.getBytes();      // 直接使用原圖
                }
                
                // 創建並儲存圖片實體
                PostPhoto postPhoto = new PostPhoto();
                postPhoto.setPostid(postId);
                postPhoto.setPostedphoto(processedImage);
                
                PostPhoto savedPhoto = postPhotoRepository.save(postPhoto);
                savedPhotos.add(savedPhoto);
                
                logger.fine("成功儲存圖片 " + photo.getOriginalFilename() + " 給貼文 " + postId);
                
            } catch (Exception e) {
                logger.log(Level.SEVERE, "儲存圖片 " + photo.getOriginalFilename() + 
                    " 給貼文 " + postId + " 時發生錯誤: " + e.getMessage(), e);
                throw new FileUploadException("儲存圖片失敗: " + photo.getOriginalFilename(), e);
            }
        }
        
        return savedPhotos;
    }
    
    /**
     * 獲取指定貼文的所有圖片
     * @param postId 貼文ID
     * @return 圖片列表
     */
    @Transactional(readOnly = true)
    public List<PostPhoto> getPhotosByPost(Integer postId) {
        return postPhotoRepository.findByPostid(postId);
    }
    
    /**
     * 獲取單張圖片
     * @param photoId 圖片ID
     * @return 圖片物件
     * @throws RuntimeException 當找不到圖片時
     */
    @Transactional(readOnly = true)
    public PostPhoto getPhoto(Integer photoId) {
        return postPhotoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("找不到圖片: " + photoId));
    }
    
    /**
     * 刪除指定圖片
     * @param photoId 要刪除的圖片ID
     */
    @Transactional
    public void deletePhoto(Integer photoId) {
        logger.fine("正在刪除圖片 " + photoId);
        
        PostPhoto photo = getPhoto(photoId);
        try {
            postPhotoRepository.delete(photo);
            logger.fine("成功刪除圖片 " + photoId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除圖片 " + photoId + " 時發生錯誤: " + e.getMessage(), e);
            throw new RuntimeException("刪除圖片失敗", e);
        }
    }
    
    /**
     * 刪除指定貼文的所有圖片
     * @param postId 貼文ID
     */
    @Transactional
    public void deletePhotosByPost(Integer postId) {
        logger.fine("正在刪除貼文 " + postId + " 的所有圖片");
        
        List<PostPhoto> photos = getPhotosByPost(postId);
        try {
            postPhotoRepository.deleteAll(photos);
            logger.fine("成功刪除貼文 " + postId + " 的 " + photos.size() + " 張圖片");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除貼文 " + postId + " 的圖片時發生錯誤: " + e.getMessage(), e);
            throw new RuntimeException("刪除圖片失敗", e);
        }
    }
    
    /**
     * 驗證圖片大小是否符合限制
     * @param file 要驗證的圖片檔案
     * @throws FileUploadException 當圖片超過大小限制時
     */
    private void validatePhotoSize(MultipartFile file) {
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new FileUploadException("檔案大小超過最大限制(10MB)");
        }
    }
    
    /**
     * 優化圖片 - 調整大小並保持品質
     * @param file 要優化的圖片檔案
     * @return 優化後的圖片位元組數組
     * @throws FileUploadException 當圖片處理失敗時
     */
    private byte[] optimizeImage(MultipartFile file) {
        try {
            // 檢查是否為GIF格式，如果是則不處理
            String contentType = file.getContentType();
            if (contentType != null && contentType.equals("image/gif")) {
                return file.getBytes();
            }

            // 讀取原始圖片
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            
            // 設定最大尺寸限制
            int maxWidth = 1080;
            int maxHeight = 1080;
            
            // 獲取原始尺寸
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            
            // 計算縮放比例
            double scale = Math.min(
                (double) maxWidth / originalWidth,
                (double) maxHeight / originalHeight
            );
            
            // 計算新的尺寸
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);
            
            // 建立新的圖片物件
            BufferedImage resizedImage = new BufferedImage(
                newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                
            // 繪製調整大小後的圖片
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();
            
            // 轉換為位元組數組
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new FileUploadException("圖片處理失敗", e);
        }
    }
}