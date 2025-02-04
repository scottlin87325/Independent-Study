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

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
@Slf4j
public class PostService {

   private final PostRepository postRepository;
   private final MemberRepository memberRepository;
   private final PostPhotoService postPhotoService;
   private final TimeUtils timeUtils;

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

   @Transactional
   public PostDTO createPost(String content, MultipartFile[] photos, Integer currentUserId) {
       return createPost(content, photos, currentUserId, false);  // 默認為非裁切模式
   }

   @Transactional(readOnly = true)
    public PostDTO getPost(Integer postId, Integer currentUserId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
        
        List<String> photoUrls = postPhotoService.getPhotosByPost(postId).stream()
            .map(photo -> "/api/post-photos/photo/" + photo.getPostphotoid())
            .collect(Collectors.toList());

        return convertToDTO(post, photoUrls, currentUserId);
    }
    
   @Transactional
   public PostDTO createPost(String content, MultipartFile[] photos, Integer currentUserId, boolean isCropped) {
       log.debug("Creating post for user ID: {}. Cropped photos: {}", currentUserId, isCropped);
       
       Member currentUser = memberRepository.findById(currentUserId)
           .orElseThrow(() -> new UnauthorizedException("找不到用戶"));
           
       Post post = new Post();
       post.setPosterid(currentUserId);
       post.setPostcontent(content);
       post.setPosttime(timeUtils.getCurrentTimeString());
       post.setLikedcount(0);
       post.setMessagecount(0);
       
       Post savedPost = postRepository.save(post);
       log.debug("Post created with ID: {}", savedPost.getPostid());
       
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

   @Transactional
   public void deletePost(Integer postId, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       if (!post.getPosterid().equals(currentUserId)) {
           log.warn("Unauthorized deletion attempt - Post: {}, User: {}", postId, currentUserId);
           throw new UnauthorizedException("您沒有權限刪除此貼文");
       }
       
       postPhotoService.deletePhotosByPost(postId);
       
       Member poster = memberRepository.findById(post.getPosterid())
           .orElseThrow(() -> new RuntimeException("找不到發文者"));
       poster.setPostcount(Math.max(0, poster.getPostcount() - 1));
       memberRepository.save(poster);
       
       postRepository.delete(post);
       log.info("Post {} successfully deleted by user {}", postId, currentUserId);
   }

   @Transactional
   public PostDTO updatePost(Integer postId, String content, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       if (!post.getPosterid().equals(currentUserId)) {
           log.warn("Unauthorized update attempt - Post: {}, User: {}", postId, currentUserId);
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

   public boolean isPostOwner(Integer postId, Integer currentUserId) {
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
       
       log.info("貼文擁有者ID: {}, 當前用戶ID: {}", post.getPosterid(), currentUserId);
       boolean isOwner = post.getPosterid().equals(currentUserId);
       log.info("所有權判斷結果: {}", isOwner);
       
       return isOwner;
   }

   private PostDTO convertToDTO(Post post, List<String> photoUrls, Integer currentUserId) {
       Member poster = memberRepository.findById(post.getPosterid())
           .orElseThrow(() -> new RuntimeException("找不到發文者"));
           
       boolean ownPost = currentUserId != null && currentUserId.equals(post.getPosterid());
       log.debug("Converting to DTO - Post: {}, User: {}, IsOwner: {}", 
                post.getPostid(), currentUserId, ownPost);
           
       return PostDTO.builder()
           .postId(post.getPostid())
           .posterId(post.getPosterid())
           .posterName(poster.getMembername())
           .postTime(post.getPosttime())
           .postContent(post.getPostcontent())
           .likedCount(post.getLikedcount())
           .messageCount(post.getMessagecount())
           .photoUrls(photoUrls)
           .posterAvatar(poster.getMemberphoto() != null ? 
               Base64.getEncoder().encodeToString(poster.getMemberphoto()) : null)
           .ownPost(ownPost)
           .canEdit(ownPost)
           .canDelete(ownPost)
           .build();
   }
}