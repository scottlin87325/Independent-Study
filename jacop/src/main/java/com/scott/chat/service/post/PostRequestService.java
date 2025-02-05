package com.scott.chat.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.scott.chat.dto.post.PostCreateRequest;
import com.scott.chat.dto.post.PostUpdateRequest;
import com.scott.chat.dto.post.PostDTO;
import com.scott.chat.model.Post;
import com.scott.chat.model.CustomUserDetails;
import com.scott.chat.repository.PostRepository;
import com.scott.chat.util.ValidationUtils;
import com.scott.chat.util.TimeUtils;

/**
* 貼文請求處理服務
* 負責處理貼文的創建和更新等請求，並進行相關驗證
*/
@Service
public class PostRequestService {
   
   private static final Logger log = LoggerFactory.getLogger(PostRequestService.class);
   
   private final PostService postService;              // 貼文核心服務
   private final ValidationUtils validationUtils;      // 驗證工具
   private final TimeUtils timeUtils;                  // 時間工具
   
   /**
    * 建構子注入相依服務
    */
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
   
   /**
    * 處理貼文創建請求(簡化版)
    * @param request 創建請求
    * @return 創建的貼文DTO
    */
   @Transactional
   public PostDTO handlePostCreation(PostCreateRequest request) {
       return handlePostCreation(request, false);
   }
   
   /**
    * 處理貼文創建請求(完整版)
    * @param request 創建請求
    * @param isCropped 圖片是否已裁切
    * @return 創建的貼文DTO
    */
   @Transactional
   public PostDTO handlePostCreation(PostCreateRequest request, boolean isCropped) {
       // 驗證請求內容
       validateCreateRequest(request);
       
       // 獲取當前登入用戶ID
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       Integer currentUserId = ((CustomUserDetails) auth.getPrincipal()).getMember().getMemberid();
       
       log.debug("正在處理貼文創建. 已裁切: {}", isCropped);
       
       // 調用核心服務創建貼文
       return postService.createPost(request.getContent(), request.getPhotos(), currentUserId, isCropped);
   }
   
   /**
    * 處理貼文更新請求
    * @param request 更新請求
    * @return 更新後的貼文DTO
    */
   @Transactional
   public PostDTO handlePostUpdate(PostUpdateRequest request) {
       // 驗證更新請求
       validateUpdateRequest(request);
       
       // 獲取當前登入用戶ID
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       Integer currentUserId = ((CustomUserDetails) auth.getPrincipal()).getMember().getMemberid();
       
       // 調用核心服務更新貼文
       return postService.updatePost(request.getPostId(), request.getContent(), currentUserId);
   }
   
   /**
    * 驗證貼文創建請求
    * @param request 創建請求
    * @throws IllegalArgumentException 當請求無效時
    */
   private void validateCreateRequest(PostCreateRequest request) {
       if (request == null) {
           throw new IllegalArgumentException("請求不能為空");
       }
       
       if (!validationUtils.isValidPostContent(request.getContent())) {
           throw new IllegalArgumentException("無效的貼文內容");
       }
       
       // 檢查上傳的圖片
       if (request.getPhotos() != null) {
           for (MultipartFile photo : request.getPhotos()) {
               if (!validationUtils.isValidPhoto(photo)) {
                   throw new IllegalArgumentException("無效的圖片格式或大小");
               }
           }
       }
   }
   
   /**
    * 驗證貼文更新請求
    * @param request 更新請求
    * @throws IllegalArgumentException 當請求無效時
    */
   private void validateUpdateRequest(PostUpdateRequest request) {
       if (request == null) {
           throw new IllegalArgumentException("請求不能為空");
       }
       
       if (request.getPostId() == null) {
           throw new IllegalArgumentException("貼文ID不能為空");
       }
       
       if (!validationUtils.isValidPostContent(request.getContent())) {
           throw new IllegalArgumentException("無效的貼文內容");
       }
   }
}