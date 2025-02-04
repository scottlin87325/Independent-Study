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
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class ImageService {
    
    @Value("${upload.path}")
    private String uploadPath;
    
    private final Path root;
    
    public ImageService(@Value("${upload.path}") String uploadPath) {
        this.root = Paths.get(uploadPath);
        init();
    }
    
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            log.error("Could not initialize folder for upload!");
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }
    
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
            log.error("Image processing failed", e);
            throw new IOException("Failed to process image: " + e.getMessage(), e);
        }
    }
    
    public String saveImage(byte[] imageData, String originalFilename) {
        try {
            String filename = generateUniqueFilename(originalFilename);
            Path targetLocation = this.root.resolve(filename);
            
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return filename;
        } catch (IOException e) {
            log.error("Failed to store image", e);
            throw new RuntimeException("Failed to store image", e);
        }
    }
    
    public Resource loadImage(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    
    public void deleteImage(String filename) {
        try {
            Path file = root.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.error("Error deleting image", e);
            throw new RuntimeException("Error deleting image", e);
        }
    }
    
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1)
                .filter(path -> !path.equals(this.root))
                .map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
    
    private String generateUniqueFilename(String originalFilename) {
        String extension = getImageExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }
    
    private String getImageExtension(String filename) {
        return StringUtils.getFilenameExtension(filename) != null ? 
            StringUtils.getFilenameExtension(filename).toLowerCase() : "jpg";
    }
    
    private void validateCropParameters(BufferedImage image, int x, int y, int width, int height) {
        if (x < 0 || y < 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid crop parameters: values must be positive");
        }
        
        if (x + width > image.getWidth() || y + height > image.getHeight()) {
            throw new IllegalArgumentException(
                String.format("Crop region (%d,%d,%d,%d) exceeds image bounds (%d,%d)",
                    x, y, width, height, image.getWidth(), image.getHeight())
            );
        }
    }
    
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
    
    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
    
    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth) throws IOException {
        double ratio = (double) targetWidth / originalImage.getWidth();
        int targetHeight = (int) (originalImage.getHeight() * ratio);
        
        return Thumbnails.of(originalImage)
            .size(targetWidth, targetHeight)
            .asBufferedImage();
    }
}