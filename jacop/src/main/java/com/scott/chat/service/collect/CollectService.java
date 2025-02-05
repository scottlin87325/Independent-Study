package com.scott.chat.service.collect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scott.chat.model.*;
import com.scott.chat.repository.*;
import com.scott.chat.dto.collect.CollectDTO;
import com.scott.chat.exception.*;
import com.scott.chat.service.post.PostPhotoService;
import com.scott.chat.util.TimeUtils;

import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;

/**
* 收藏服務類
* 處理貼文收藏相關的業務邏輯
*/
@Service
public class CollectService {

   private static final Logger log = LoggerFactory.getLogger(CollectService.class);
   
   private final CollectRepository collectRepository;       // 收藏資料存取
   private final PostRepository postRepository;             // 貼文資料存取
   private final MemberRepository memberRepository;         // 會員資料存取
   private final TimeUtils timeUtils;                      // 時間工具類
   
   @Autowired
   private PostPhotoService postPhotoService;              // 貼文圖片服務

   /**
    * 建構子依賴注入
    */
   public CollectService(
       CollectRepository collectRepository,
       PostRepository postRepository,
       MemberRepository memberRepository,
       TimeUtils timeUtils
   ) {
       this.collectRepository = collectRepository;
       this.postRepository = postRepository;
       this.memberRepository = memberRepository;
       this.timeUtils = timeUtils;
   }

   /**
    * 收藏貼文
    * @param postId 貼文ID
    * @param collecterId 收藏者ID
    * @return 收藏資訊DTO
    */
   @Transactional
   public CollectDTO collectPost(Integer postId, Integer collecterId) {
       if (isPostCollectedByUser(postId, collecterId)) {
           throw new InvalidRequestException("貼文已被收藏");
       }
       
       Post post = postRepository.findById(postId)
           .orElseThrow(() -> new PostNotFoundException(postId));
           
       Member collector = memberRepository.findById(collecterId)
           .orElseThrow(() -> new UnauthorizedException("找不到使用者"));
           
       Collect collect = new Collect();
       collect.setPostid(postId);
       collect.setCollecterid(collecterId);
       collect.setCollectedcount(1);
       
       Collect savedCollect = collectRepository.save(collect);
       
       return convertToDTO(savedCollect, post);
   }

   /**
    * 取消收藏貼文
    * @param postId 貼文ID
    * @param collecterId 收藏者ID
    */
   @Transactional
   public void uncollectPost(Integer postId, Integer collecterId) {
       collectRepository.deleteByPostidAndCollecterid(postId, collecterId);
   }

   /**
    * 檢查用戶是否已收藏貼文
    * @param postId 貼文ID
    * @param collecterId 收藏者ID
    * @return 是否已收藏
    */
   public boolean isPostCollectedByUser(Integer postId, Integer collecterId) {
       return collectRepository.existsByPostidAndCollecterid(postId, collecterId);
   }

   /**
    * 獲取用戶收藏列表
    * @param collecterId 收藏者ID
    * @param page 頁碼
    * @param size 每頁筆數
    * @return 收藏DTO列表
    */
   @Transactional(readOnly = true)
   public List<CollectDTO> getCollectsByUser(Integer collecterId, int page, int size) {
       PageRequest pageRequest = PageRequest.of(page, size, Sort.by("collectid").descending());
       
       return collectRepository.findByCollecterid(collecterId, pageRequest)
           .stream()
           .map(collect -> {
               Post post = postRepository.findById(collect.getPostid())
                   .orElseThrow(() -> new PostNotFoundException(collect.getPostid()));
               return convertToDTO(collect, post);
           })
           .collect(Collectors.toList());
   }

   /**
    * 獲取貼文收藏數
    * @param postId 貼文ID
    * @return 收藏數量
    */
   @Transactional(readOnly = true)
   public int getCollectCountByPost(Integer postId) {
       return collectRepository.countByPostid(postId);
   }

   /**
    * 將收藏實體轉換為DTO
    * @param collect 收藏實體
    * @param post 貼文實體
    * @return 收藏DTO
    */
   private CollectDTO convertToDTO(Collect collect, Post post) {
       Member postOwner = memberRepository.findById(post.getPosterid())
           .orElseThrow(() -> new RuntimeException("找不到貼文作者"));
           
       Member collector = memberRepository.findById(collect.getCollecterid())
           .orElseThrow(() -> new RuntimeException("找不到收藏者"));
       
       String firstPhotoUrl = null;
       List<PostPhoto> photos = postPhotoService.getPhotosByPost(post.getPostid());
       if (!photos.isEmpty()) {
           firstPhotoUrl = "/api/post-photos/photo/" + photos.get(0).getPostphotoid();
       }
           
       CollectDTO dto = new CollectDTO();
       dto.setCollectId(collect.getCollectid());
       dto.setPostId(post.getPostid());
       dto.setCollecterId(collect.getCollecterid());
       dto.setCollectTime(timeUtils.getCurrentTimeString());
       dto.setPostContent(post.getPostcontent());
       dto.setPostPhotoUrl(firstPhotoUrl);
       dto.setPostOwnerId(postOwner.getMemberid());
       dto.setPostOwnerName(postOwner.getMembername());
       dto.setPostOwnerAvatar(postOwner.getMemberphoto() != null ? 
           Base64.getEncoder().encodeToString(postOwner.getMemberphoto()) : null);
       dto.setCollectedCount(collect.getCollectedcount());
       
       return dto;
   }
}