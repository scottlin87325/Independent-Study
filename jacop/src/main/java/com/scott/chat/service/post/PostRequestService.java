package com.scott.chat.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.scott.chat.dto.post.PostCreateRequest;
import com.scott.chat.dto.post.PostUpdateRequest;
import com.scott.chat.dto.post.PostDTO;
import com.scott.chat.model.Post;
import com.scott.chat.model.CustomUserDetails;
import com.scott.chat.repository.PostRepository;
import com.scott.chat.util.ValidationUtils;
import com.scott.chat.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostRequestService {
    
    private final PostService postService;
    private final ValidationUtils validationUtils;
    private final TimeUtils timeUtils;
    
    @Autowired
    public PostRequestService(
        PostService postService,
        ValidationUtils validationUtils,
        TimeUtils timeUtils
    ) {
        this.postService = postService;
        this.validationUtils = validationUtils;
        this.timeUtils = timeUtils;
    }
    
    @Transactional
    public PostDTO handlePostCreation(PostCreateRequest request) {
        return handlePostCreation(request, false);
    }

    @Transactional
    public PostDTO handlePostCreation(PostCreateRequest request, boolean isCropped) {
        // 驗證請求
        validateCreateRequest(request);
        
        // 獲取當前用戶ID
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails) auth.getPrincipal()).getMember().getMemberid();
        
        log.debug("Handling post creation. Cropped: {}", isCropped);
        
        // 處理貼文創建
        return postService.createPost(request.getContent(), request.getPhotos(), currentUserId, isCropped);
    }
    
    @Transactional
    public PostDTO handlePostUpdate(PostUpdateRequest request) {
        // 驗證請求
        validateUpdateRequest(request);
        
        // 獲取當前用戶ID
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails) auth.getPrincipal()).getMember().getMemberid();
        
        // 處理貼文更新
        return postService.updatePost(request.getPostId(), request.getContent(), currentUserId);
    }
    
    private void validateCreateRequest(PostCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        if (!validationUtils.isValidPostContent(request.getContent())) {
            throw new IllegalArgumentException("Invalid post content");
        }
        
        if (request.getPhotos() != null) {
            for (MultipartFile photo : request.getPhotos()) {
                if (!validationUtils.isValidPhoto(photo)) {
                    throw new IllegalArgumentException("Invalid photo format or size");
                }
            }
        }
    }
    
    private void validateUpdateRequest(PostUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        if (request.getPostId() == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        
        if (!validationUtils.isValidPostContent(request.getContent())) {
            throw new IllegalArgumentException("Invalid post content");
        }
    }
}