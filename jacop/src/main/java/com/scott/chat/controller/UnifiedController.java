package com.scott.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;

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

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
@Slf4j
public class UnifiedController {

   private final MessageService messageService;
   private final CollectService collectService;
   private final PostService postService;
   private final PostPhotoService postPhotoService;
   private final CommentService commentService;
   private final ImageService imageService;

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

   private Member getCurrentMember(Authentication authentication) {
       if (authentication == null || !authentication.isAuthenticated() || 
           !(authentication.getPrincipal() instanceof CustomUserDetails)) {
           log.warn("Authentication failed or invalid");
           throw new UnauthorizedException("請先登入");
       }
       CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
       Member member = userDetails.getMember();
       log.debug("Current member ID: {}", member.getMemberid());
       return member;
   }

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
           log.error("圖片處理失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

   @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<?> createPost(
           @RequestParam("content") String content,
           @RequestParam(value = "photos", required = false) MultipartFile[] photos,
           Authentication authentication) {
       try {
           Member currentMember = getCurrentMember(authentication);
           log.debug("Creating post for user: {}", currentMember.getMembername());
           
           PostDTO post = postService.createPost(content, photos, currentMember.getMemberid());
           return ResponseEntity.ok(post);
       } catch (Exception e) {
           log.error("建立貼文失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("獲取貼文失敗: {}", e.getMessage());
           return ResponseEntity.badRequest().build();
       }
   }

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
           log.error("獲取貼文列表失敗", e);
           return ResponseEntity.badRequest().build();
       }
   }

   @DeleteMapping("/posts/{postId}")
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<?> deletePost(@PathVariable Integer postId, 
           Authentication authentication) {
       try {
           Member currentMember = getCurrentMember(authentication);
           log.debug("Delete request - Post: {}, User: {}", postId, currentMember.getMemberid());

           if (!postService.isPostOwner(postId, currentMember.getMemberid())) {
               log.warn("Permission denied - User {} attempted to delete post {}", 
                   currentMember.getMemberid(), postId);
               return ResponseEntity.status(HttpStatus.FORBIDDEN)
                   .body(Map.of("error", "您沒有權限刪除此貼文"));
           }
           
           postService.deletePost(postId, currentMember.getMemberid());
           log.debug("Post {} deleted successfully by user {}", postId, currentMember.getMemberid());
           return ResponseEntity.ok().build();
           
       } catch (PostNotFoundException e) {
           log.error("Post not found: {}", postId);
           return ResponseEntity.notFound().build();
       } catch (Exception e) {
           log.error("Delete post failed - Post: {}, Error: {}", postId, e.getMessage(), e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("更新貼文失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("更新讚數失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("新增留言失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

   @GetMapping("/posts/{postId}/comments")
   public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer postId) {
       try {
           List<CommentDTO> comments = commentService.getPostComments(postId);
           return ResponseEntity.ok(comments);
       } catch (PostNotFoundException e) {
           return ResponseEntity.notFound().build();
       } catch (Exception e) {
           log.error("獲取留言失敗", e);
           return ResponseEntity.badRequest().build();
       }
   }

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
           log.error("刪除留言失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("添加照片失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

   @GetMapping("/post-photos/{postId}")
   public ResponseEntity<List<PostPhoto>> getPostPhotos(@PathVariable Integer postId) {
       try {
           List<PostPhoto> photos = postPhotoService.getPhotosByPost(postId);
           return ResponseEntity.ok(photos);
       } catch (Exception e) {
           log.error("獲取照片失敗", e);
           return ResponseEntity.badRequest().build();
       }
   }

   @GetMapping("/post-photos/photo/{photoId}")
   public ResponseEntity<byte[]> getPhotoContent(@PathVariable Integer photoId) {
       try {
           PostPhoto photo = postPhotoService.getPhoto(photoId);
           return ResponseEntity.ok()
               .contentType(MediaType.IMAGE_JPEG)
               .body(photo.getPostedphoto());
       } catch (Exception e) {
           log.error("獲取照片內容失敗", e);
           return ResponseEntity.notFound().build();
       }
   }

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
           log.error("刪除照片失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

   @PostMapping("/collects/{postId}")
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<?> collectPost(@PathVariable Integer postId, 
           Authentication authentication) {
       try {
           Member currentMember = getCurrentMember(authentication);
           CollectDTO collect = collectService.collectPost(postId, currentMember.getMemberid());
           return ResponseEntity.ok(collect);
       } catch (Exception e) {
           log.error("收藏貼文失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

   @DeleteMapping("/collects/{postId}")
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<?> uncollectPost(@PathVariable Integer postId, 
           Authentication authentication) {
       try {
           Member currentMember = getCurrentMember(authentication);
           collectService.uncollectPost(postId, currentMember.getMemberid());
           return ResponseEntity.ok().build();
       } catch (Exception e) {
           log.error("取消收藏失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

   @GetMapping("/collects/user/{collecterId}")
   public ResponseEntity<List<CollectDTO>> getUserCollects(
           @PathVariable Integer collecterId,
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "10") int size) {
       try {
           List<CollectDTO> collects = collectService.getCollectsByUser(collecterId, page, size);
           return ResponseEntity.ok(collects);
       } catch (Exception e) {
           log.error("獲取收藏列表失敗", e);
           return ResponseEntity.badRequest().build();
       }
   }

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
           log.error("檢查收藏狀態失敗", e);
           return ResponseEntity.badRequest().body(false);
       }
   }

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
           log.error("獲取留言失敗", e);
           return ResponseEntity.badRequest().build();
       }
   }

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
           log.error("建立留言失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("刪除留言失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }

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
           log.error("更新留言失敗", e);
           return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
       }
   }
}