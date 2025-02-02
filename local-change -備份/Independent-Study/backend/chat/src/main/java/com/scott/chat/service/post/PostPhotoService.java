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

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
@Slf4j
public class PostPhotoService {
    
    private final PostPhotoRepository postPhotoRepository;
    private final FileUtils fileUtils;
    private final ValidationUtils validationUtils;
    
    @Value("${upload.path}")
    private String uploadPath;
    
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
    
    @Transactional
    public List<PostPhoto> savePhotos(Integer postId, MultipartFile[] photos) {
        log.debug("Saving photos for post {}", postId);
        
        if (photos == null || photos.length == 0) {
            return Collections.emptyList();
        }
        
        // 檢查照片數量限制
        if (photos.length > 10) {
            throw new InvalidRequestException("Maximum 10 photos allowed per post");
        }
        
        List<PostPhoto> savedPhotos = new ArrayList<>();
        
        for (MultipartFile photo : photos) {
            try {
                // 驗證圖片
                if (!validationUtils.isValidPhoto(photo)) {
                    throw new FileUploadException("Invalid photo format or size: " + photo.getOriginalFilename());
                }
                
                PostPhoto postPhoto = new PostPhoto();
                postPhoto.setPostid(postId);
                postPhoto.setPostedphotofile(photo);
                
                // 檢查圖片是否成功保存
                if (postPhoto.getPostedphoto() == null) {
                    throw new FileUploadException("Failed to save photo data");
                }
                
                PostPhoto savedPhoto = postPhotoRepository.save(postPhoto);
                savedPhotos.add(savedPhoto);
                
                log.debug("Successfully saved photo {} for post {}", 
                    photo.getOriginalFilename(), postId);
                
            } catch (Exception e) {
                log.error("Error saving photo {} for post {}: {}", 
                    photo.getOriginalFilename(), postId, e.getMessage());
                throw new FileUploadException("Failed to save photo: " + photo.getOriginalFilename(), e);
            }
        }
        
        return savedPhotos;
    }
    
    @Transactional(readOnly = true)
    public List<PostPhoto> getPhotosByPost(Integer postId) {
        return postPhotoRepository.findByPostid(postId);
    }
    
    @Transactional(readOnly = true)
    public PostPhoto getPhoto(Integer photoId) {
        return postPhotoRepository.findById(photoId)
            .orElseThrow(() -> new RuntimeException("Photo not found: " + photoId));
    }
    
    @Transactional
    public void deletePhoto(Integer photoId) {
        log.debug("Deleting photo {}", photoId);
        
        PostPhoto photo = getPhoto(photoId);
        try {
            postPhotoRepository.delete(photo);
            log.debug("Successfully deleted photo {}", photoId);
        } catch (Exception e) {
            log.error("Error deleting photo {}: {}", photoId, e.getMessage());
            throw new RuntimeException("Failed to delete photo", e);
        }
    }
    
    @Transactional
    public void deletePhotosByPost(Integer postId) {
        log.debug("Deleting all photos for post {}", postId);
        
        List<PostPhoto> photos = getPhotosByPost(postId);
        try {
            postPhotoRepository.deleteAll(photos);
            log.debug("Successfully deleted {} photos for post {}", 
                photos.size(), postId);
        } catch (Exception e) {
            log.error("Error deleting photos for post {}: {}", postId, e.getMessage());
            throw new RuntimeException("Failed to delete photos", e);
        }
    }
    
    // 檢查圖片大小限制
    private void validatePhotoSize(MultipartFile file) {
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new FileUploadException("File size exceeds maximum limit (10MB)");
        }
    }
    
    // 優化圖片並調整大小
    private byte[] optimizeImage(MultipartFile file) {
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            
            // 設定最大尺寸
            int maxWidth = 1080;
            int maxHeight = 1080;
            
            // 計算新尺寸
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            
            double scale = Math.min(
                (double) maxWidth / originalWidth,
                (double) maxHeight / originalHeight
            );
            
            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);
            
            // 調整圖片大小
            BufferedImage resizedImage = new BufferedImage(
                newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();
            
            // 轉換為 byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            return baos.toByteArray();
            
        } catch (IOException e) {
            throw new FileUploadException("Failed to process image", e);
        }
    }
}