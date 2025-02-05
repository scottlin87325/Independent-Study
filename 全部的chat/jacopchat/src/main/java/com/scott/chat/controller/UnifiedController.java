package com.scott.chat.controller;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.scott.chat.service.message.MessageService;
import com.scott.chat.service.collect.CollectService;
import com.scott.chat.service.post.PostService;
import com.scott.chat.service.post.PostPhotoService;
import com.scott.chat.service.comment.CommentService;
import com.scott.chat.service.ImageService;
import com.scott.chat.model.Member;
import com.scott.chat.model.CustomUserDetails;
import com.scott.chat.dto.message.MessageDTO;
import com.scott.chat.dto.collect.CollectDTO;
import com.scott.chat.dto.post.PostDTO;
import com.scott.chat.dto.comment.CommentDTO;
import com.scott.chat.model.PostPhoto;
import com.scott.chat.exception.UnauthorizedException;
import com.scott.chat.exception.PostNotFoundException;

import java.util.List;
import java.util.Map;

/**
 * 統一的REST API控制器
 * 處理貼文、留言、收藏、圖片等功能
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class UnifiedController {

    // 日誌記錄器
    private static final Logger logger = Logger.getLogger(UnifiedController.class.getName());

    // 注入的服務實例
    private final MessageService messageService;
    private final CollectService collectService;
    private final PostService postService;
    private final PostPhotoService postPhotoService;
    private final CommentService commentService;
    private final ImageService imageService;

    /**
     * 注入所需服務的建構子
     */
    @Autowired
    public UnifiedController(MessageService messageService, 
                           CollectService collectService, 
                           PostService postService, 
                           PostPhotoService postPhotoService,
                           CommentService commentService,
                           ImageService imageService) {
        this.messageService = messageService;
        this.collectService = collectService;
        this.postService = postService;
        this.postPhotoService = postPhotoService;
        this.commentService = commentService;
        this.imageService = imageService;
    }

    /**
     * 獲取當前登入用戶
     * @throws UnauthorizedException 用戶未登入時拋出
     */
    private Member getCurrentMember(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || 
            !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            logger.warning("Authentication failed or invalid");
            throw new UnauthorizedException("請先登入");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();
        logger.fine("Current member ID: " + member.getMemberid());
        return member;
    }

    /**
     * 處理圖片上傳並裁剪
     */
    @PostMapping("/posts/upload-photo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("image") MultipartFile image,
            @RequestParam("x") int x,
            @RequestParam("y") int y,
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            byte[] processedImage = imageService.cropAndResizeImage(image, x, y, width, height);
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(processedImage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "圖片處理失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 建立新貼文
     */
    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "photos", required = false) MultipartFile[] photos,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            logger.fine("Creating post for user: " + currentMember.getMembername());
            
            PostDTO post = postService.createPost(content, photos, currentMember.getMemberid());
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "建立貼文失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 獲取特定貼文
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Integer postId, 
            Authentication authentication) {
        try {
            Integer currentUserId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                currentUserId = getCurrentMember(authentication).getMemberid();
            }
            PostDTO post = postService.getPost(postId, currentUserId);
            return ResponseEntity.ok(post);
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取貼文失敗: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 獲取貼文列表，支援分頁
     */
    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try {
            Integer currentUserId = null;
            if (authentication != null && authentication.isAuthenticated()) {
                currentUserId = getCurrentMember(authentication).getMemberid();
            }
            List<PostDTO> posts = postService.getPosts(page, size, currentUserId);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取貼文列表失敗", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 刪除貼文，需要是貼文擁有者
     */
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePost(@PathVariable Integer postId, 
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            logger.fine("Delete request - Post: " + postId + ", User: " + currentMember.getMemberid());

            if (!postService.isPostOwner(postId, currentMember.getMemberid())) {
                logger.warning("Permission denied - User " + currentMember.getMemberid() + 
                    " attempted to delete post " + postId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "您沒有權限刪除此貼文"));
            }
            
            postService.deletePost(postId, currentMember.getMemberid());
            logger.fine("Post " + postId + " deleted successfully by user " + currentMember.getMemberid());
            return ResponseEntity.ok().build();
            
        } catch (PostNotFoundException e) {
            logger.log(Level.SEVERE, "Post not found: " + postId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Delete post failed - Post: " + postId + ", Error: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新貼文內容
     */
    @PutMapping("/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updatePost(
            @PathVariable Integer postId,
            @RequestParam("content") String content,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            if (!postService.isPostOwner(postId, currentMember.getMemberid())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "您沒有權限修改此貼文"));
            }
            PostDTO updatedPost = postService.updatePost(postId, content, currentMember.getMemberid());
            return ResponseEntity.ok(updatedPost);
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "更新貼文失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新貼文按讚狀態
     */
    @PutMapping("/posts/{postId}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateLike(@PathVariable Integer postId, 
            @RequestParam boolean isLike,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            postService.updateLikeCount(postId, isLike);
            return ResponseEntity.ok().build();
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "更新讚數失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 新增貼文留言
     */
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addComment(
            @PathVariable Integer postId,
            @RequestParam String content,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            CommentDTO comment = commentService.createComment(postId, currentMember.getMemberid(), content);
            return ResponseEntity.ok(comment);
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "新增留言失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 獲取貼文的所有留言
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer postId) {
        try {
            List<CommentDTO> comments = commentService.getPostComments(postId);
            return ResponseEntity.ok(comments);
        } catch (PostNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取留言失敗", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 刪除貼文留言
     */
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(
            @PathVariable Integer postId,
            @PathVariable Long commentId,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            commentService.deleteComment(commentId, currentMember.getMemberid());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除留言失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 新增照片到貼文
     */
    @PostMapping("/post-photos/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addPhotosToPost(
            @PathVariable Integer postId,
            @RequestParam("photos") MultipartFile[] photos,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            if (!postService.isPostOwner(postId, currentMember.getMemberid())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "您沒有權限新增照片"));
            }
            List<PostPhoto> savedPhotos = postPhotoService.savePhotos(postId, photos);
            return ResponseEntity.ok(savedPhotos);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "添加照片失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 獲取貼文的所有照片
     */
    @GetMapping("/post-photos/{postId}")
    public ResponseEntity<List<PostPhoto>> getPostPhotos(@PathVariable Integer postId) {
        try {
            List<PostPhoto> photos = postPhotoService.getPhotosByPost(postId);
            return ResponseEntity.ok(photos);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取照片失敗", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 獲取單張照片內容
     */
    @GetMapping("/post-photos/photo/{photoId}")
    public ResponseEntity<byte[]> getPhotoContent(@PathVariable Integer photoId) {
        try {
            PostPhoto photo = postPhotoService.getPhoto(photoId);
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(photo.getPostedphoto());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取照片內容失敗", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 刪除照片
     */
    @DeleteMapping("/post-photos/{photoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deletePhoto(
            @PathVariable Integer photoId,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            PostPhoto photo = postPhotoService.getPhoto(photoId);
            if (!postService.isPostOwner(photo.getPostid(), currentMember.getMemberid())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "您沒有權限刪除照片"));
            }
            postPhotoService.deletePhoto(photoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除照片失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 收藏貼文
     */
    @PostMapping("/collects/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> collectPost(@PathVariable Integer postId, 
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            CollectDTO collect = collectService.collectPost(postId, currentMember.getMemberid());
            return ResponseEntity.ok(collect);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "收藏貼文失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 取消收藏貼文
     */
    @DeleteMapping("/collects/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uncollectPost(@PathVariable Integer postId, 
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            collectService.uncollectPost(postId, currentMember.getMemberid());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "取消收藏失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 獲取用戶的收藏列表
     */
    @GetMapping("/collects/user/{collecterId}")
    public ResponseEntity<List<CollectDTO>> getUserCollects(
            @PathVariable Integer collecterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<CollectDTO> collects = collectService.getCollectsByUser(collecterId, page, size);
            return ResponseEntity.ok(collects);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取收藏列表失敗", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 檢查貼文是否已被收藏
     */
    @GetMapping("/collects/check")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> checkIfCollected(
            @RequestParam Integer postId,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            boolean isCollected = collectService.isPostCollectedByUser(postId, currentMember.getMemberid());
            return ResponseEntity.ok(isCollected);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "檢查收藏狀態失敗", e);
            return ResponseEntity.badRequest().body(false);
        }
    }

    /**
     * 獲取貼文的訊息列表
     */
    @GetMapping("/messages/post/{postId}")
    public ResponseEntity<List<MessageDTO>> getPostMessages(
            @PathVariable Integer postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByPost(postId, 
                PageRequest.of(page, size));
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "獲取留言失敗", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 建立新訊息
     */
    @PostMapping("/messages/{postId}/comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createMessage(
            @PathVariable Integer postId,
            @RequestParam("message") String message,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            MessageDTO newMessage = messageService.createMessage(postId, message, file, 
                currentMember.getMemberid());
            return ResponseEntity.ok(newMessage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "建立留言失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 刪除訊息
     */
    @DeleteMapping("/messages/{messageId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Integer messageId,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            messageService.deleteMessage(messageId, currentMember.getMemberid());
            return ResponseEntity.ok().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "您沒有權限刪除此留言"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "刪除留言失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新訊息內容
     */
    @PutMapping("/messages/{messageId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMessage(
            @PathVariable Integer messageId,
            @RequestBody String updatedMessage,
            Authentication authentication) {
        try {
            Member currentMember = getCurrentMember(authentication);
            MessageDTO updated = messageService.updateMessage(messageId, updatedMessage, 
                currentMember.getMemberid());
            return ResponseEntity.ok(updated);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "您沒有權限修改此留言"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "更新留言失敗", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}