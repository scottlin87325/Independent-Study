package com.scott.chat.service.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.scott.chat.model.Post;
import com.scott.chat.model.PostPhoto;
import com.scott.chat.model.Member;
import com.scott.chat.repository.PostRepository;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.repository.PostPhotoRepository;
import com.scott.chat.dto.post.PostDTO;
import com.scott.chat.exception.PostNotFoundException;
import com.scott.chat.exception.UnauthorizedException;
import com.scott.chat.util.TimeUtils;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Base64;

/**
* 貼文服務類
* 處理貼文的CRUD操作與相關業務邏輯
*/
@Service 
public class PostService {
   
   private static final Logger logger = Logger.getLogger(PostService.class.getName());

   private final PostRepository postRepository;           // 貼文資料存取
   private final MemberRepository memberRepository;       // 會員資料存取
   private final PostPhotoService postPhotoService;      // 貼文圖片服務
   private final TimeUtils timeUtils;                    // 時間工具類

   /**
    * 建構子
    */
   @Autowired
   public PostService(PostRepository postRepository, 
                     MemberRepository memberRepository,
                     PostPhotoService postPhotoService, 
                     TimeUtils timeUtils) {
       this.postRepository = postRepository;
       this.memberRepository = memberRepository;
       this.postPhotoService = postPhotoService;
       this.timeUtils = timeUtils;
   }

   /**
    * 創建貼文簡化版
    */
   @Transactional
   public PostDTO createPost(String content, MultipartFile[] photos, Integer currentUserId) {
       return createPost(content, photos, currentUserId, false);
   }

   /**
    * 獲取單一貼文
    */
   @Transactional(readOnly = true)
   public PostDTO getPost(Integer postId, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
       
       List<String> photoUrls = postPhotoService.getPhotosByPost(postId).stream()
           .map(photo -> "/api/post-photos/photo/" + photo.getPostphotoid())
           .collect(Collectors.toList());

       return convertToDTO(post, photoUrls, currentUserId);
   }
   
   /**
    * 創建貼文完整版
    */
   @Transactional
   public PostDTO createPost(String content, MultipartFile[] photos, Integer currentUserId, boolean isCropped) {
       logger.fine("開始創建貼文，用戶ID: " + currentUserId + "，圖片裁切: " + isCropped);
       
       Member currentUser = memberRepository.findById(currentUserId)
           .orElseThrow(() -> new UnauthorizedException("找不到用戶"));
           
       Post post = new Post();
       post.setPosterid(currentUserId);
       post.setPostcontent(content);
       post.setPosttime(timeUtils.getCurrentTimeString());
       post.setLikedcount(0);
       post.setMessagecount(0);
       
       Post savedPost = postRepository.save(post);
       logger.fine("貼文已創建，ID: " + savedPost.getPostid());
       
       List<String> photoUrls = new ArrayList<>();
       if (photos != null && photos.length > 0) {
           List<PostPhoto> savedPhotos = postPhotoService.savePhotos(savedPost.getPostid(), photos, isCropped);
           photoUrls = savedPhotos.stream()
               .map(photo -> "/api/post-photos/photo/" + photo.getPostphotoid())
               .collect(Collectors.toList());
       }
       
       currentUser.setPostcount(currentUser.getPostcount() != null ? 
           currentUser.getPostcount() + 1 : 1);
       memberRepository.save(currentUser);
       
       return convertToDTO(savedPost, photoUrls, currentUserId);
   }

   /**
    * 獲取貼文列表(分頁)
    */
   public List<PostDTO> getPosts(int page, int size, Integer currentUserId) {
       PageRequest pageRequest = PageRequest.of(page, size, 
           Sort.by("posttime").descending());
           
       Page<Post> posts = postRepository.findAll(pageRequest);
       
       return posts.getContent().stream()
           .map(post -> {
               List<String> photoUrls = postPhotoService.getPhotosByPost(post.getPostid())
                   .stream()
                   .map(photo -> "/api/post-photos/photo/" + photo.getPostphotoid())
                   .collect(Collectors.toList());
               return convertToDTO(post, photoUrls, currentUserId);
           })
           .collect(Collectors.toList());
   }

   /**
    * 刪除貼文
    */
   @Transactional
   public void deletePost(Integer postId, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       if (!post.getPosterid().equals(currentUserId)) {
           logger.warning("未經授權的刪除嘗試 - 貼文: " + postId + "，用戶: " + currentUserId);
           throw new UnauthorizedException("您沒有權限刪除此貼文");
       }
       
       postPhotoService.deletePhotosByPost(postId);
       
       Member poster = memberRepository.findById(post.getPosterid())
           .orElseThrow(() -> new RuntimeException("找不到發文者"));
       poster.setPostcount(Math.max(0, poster.getPostcount() - 1));
       memberRepository.save(poster);
       
       postRepository.delete(post);
       logger.info("貼文 " + postId + " 已被用戶 " + currentUserId + " 成功刪除");
   }

   /**
    * 更新貼文
    */
   @Transactional
   public PostDTO updatePost(Integer postId, String content, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       if (!post.getPosterid().equals(currentUserId)) {
           logger.warning("未經授權的更新嘗試 - 貼文: " + postId + "，用戶: " + currentUserId);
           throw new UnauthorizedException("您沒有權限修改此貼文");
       }
           
       post.setPostcontent(content);
       Post updatedPost = postRepository.save(post);
       
       List<String> photoUrls = postPhotoService.getPhotosByPost(postId)
           .stream()
           .map(photo -> "/api/post-photos/photo/" + photo.getPostphotoid())
           .collect(Collectors.toList());
           
       return convertToDTO(updatedPost, photoUrls, currentUserId);
   }

   /**
    * 更新按讚數
    */
   @Transactional
   public void updateLikeCount(Integer postId, boolean isIncrease) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       if (isIncrease) {
           post.setLikedcount(post.getLikedcount() + 1);
       } else {
           post.setLikedcount(Math.max(0, post.getLikedcount() - 1));
       }
       
       postRepository.save(post);
   }

   /**
    * 更新留言數
    */
   @Transactional
   public void updateMessageCount(Integer postId, boolean isIncrease) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       if (isIncrease) {
           post.setMessagecount(post.getMessagecount() + 1);
       } else {
           post.setMessagecount(Math.max(0, post.getMessagecount() - 1));
       }
       
       postRepository.save(post);
   }

   /**
    * 檢查是否為貼文擁有者
    */
   public boolean isPostOwner(Integer postId, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
       
       logger.info("貼文擁有者ID: " + post.getPosterid() + ", 當前用戶ID: " + currentUserId);
       boolean isOwner = post.getPosterid().equals(currentUserId);
       logger.info("所有權判斷結果: " + isOwner);
       
       return isOwner;
   }

   /**
    * 將貼文實體轉換為DTO
    */
   private PostDTO convertToDTO(Post post, List<String> photoUrls, Integer currentUserId) {
       Member poster = memberRepository.findById(post.getPosterid())
           .orElseThrow(() -> new RuntimeException("找不到發文者"));
           
       boolean ownPost = currentUserId != null && currentUserId.equals(post.getPosterid());
       logger.fine("轉換為DTO - 貼文: " + post.getPostid() + "，用戶: " + 
                   currentUserId + "，是否為擁有者: " + ownPost);
           
       PostDTO dto = new PostDTO();
       dto.setPostId(post.getPostid());
       dto.setPosterId(post.getPosterid());
       dto.setPosterName(poster.getMembername());
       dto.setPostTime(post.getPosttime());
       dto.setPostContent(post.getPostcontent());
       dto.setLikedCount(post.getLikedcount());
       dto.setMessageCount(post.getMessagecount());
       dto.setPhotoUrls(photoUrls);
       dto.setPosterAvatar(poster.getMemberphoto() != null ? 
           Base64.getEncoder().encodeToString(poster.getMemberphoto()) : null);
       dto.setOwnPost(ownPost);
       dto.setCanEdit(ownPost);
       dto.setCanDelete(ownPost);
       
       return dto;
   }
}