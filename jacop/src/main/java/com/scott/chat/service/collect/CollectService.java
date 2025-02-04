package com.scott.chat.service.collect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.scott.chat.model.Collect;
import com.scott.chat.model.Post;
import com.scott.chat.model.Member;
import com.scott.chat.model.PostPhoto;
import com.scott.chat.repository.CollectRepository;
import com.scott.chat.repository.PostRepository;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.dto.collect.CollectDTO;
import com.scott.chat.exception.InvalidRequestException;
import com.scott.chat.exception.PostNotFoundException;
import com.scott.chat.exception.UnauthorizedException;
import com.scott.chat.service.post.PostPhotoService;
import com.scott.chat.util.TimeUtils;
// 工具類引入
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Base64;
import java.util.stream.Collectors;
// 收藏服務類
@Service
@Slf4j
public class CollectService {
    // 注入所需的資料庫訪問對象和工具類
    private final CollectRepository collectRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TimeUtils timeUtils;
    // 注入貼文圖片服務
    @Autowired
    private PostPhotoService postPhotoService;
    // 建構子注入依賴
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
    // 收藏貼文
    // 創建新的收藏記錄並返回相關資訊
    @Transactional
    public CollectDTO collectPost(Integer postId, Integer collecterId) {
        // 檢查是否已經收藏
        if (isPostCollectedByUser(postId, collecterId)) {
            throw new InvalidRequestException("Post already collected");
        }
        
        // 獲取貼文和收藏者資訊
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
            
        Member collector = memberRepository.findById(collecterId)
            .orElseThrow(() -> new UnauthorizedException("User not found"));
            
        // 創建收藏記錄
        Collect collect = new Collect();
        collect.setPostid(postId);
        collect.setCollecterid(collecterId);
        collect.setCollectedcount(1);
        
        Collect savedCollect = collectRepository.save(collect);
        
        return convertToDTO(savedCollect, post);
    }
    // 取消收藏貼文
    // 根據貼文ID和收藏者ID刪除收藏記錄
    @Transactional
    public void uncollectPost(Integer postId, Integer collecterId) {
        collectRepository.deleteByPostidAndCollecterid(postId, collecterId);
    }
     // 檢查用戶是否已收藏該貼文
    public boolean isPostCollectedByUser(Integer postId, Integer collecterId) {
        return collectRepository.existsByPostidAndCollecterid(postId, collecterId);
    }
    // 獲取用戶的收藏列表
    // 支援分頁查詢，按收藏ID降序排序
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
    // 獲取貼文的收藏數量
    @Transactional(readOnly = true)
    public int getCollectCountByPost(Integer postId) {
        return collectRepository.countByPostid(postId);
    }
    // 將收藏實體轉換為DTO對象
    // 包含收藏相關信息、貼文內容、作者信息等
    private CollectDTO convertToDTO(Collect collect, Post post) {
        // 獲取貼文作者資訊
        Member postOwner = memberRepository.findById(post.getPosterid())
            .orElseThrow(() -> new RuntimeException("Post owner not found"));
            
        // 獲取收藏者資訊
        Member collector = memberRepository.findById(collect.getCollecterid())
            .orElseThrow(() -> new RuntimeException("Collector not found"));
        
        // 獲取貼文的第一張圖片（如果有的話）
        String firstPhotoUrl = null;
        List<PostPhoto> photos = postPhotoService.getPhotosByPost(post.getPostid());
        if (!photos.isEmpty()) {
            firstPhotoUrl = "/api/post-photos/photo/" + photos.get(0).getPostphotoid();
        }
            
        return CollectDTO.builder()
            .collectId(collect.getCollectid())
            .postId(post.getPostid())
            .collecterId(collect.getCollecterid())
            .collectTime(timeUtils.getCurrentTimeString())
            .postContent(post.getPostcontent())
            .postPhotoUrl(firstPhotoUrl)
            .postOwnerId(postOwner.getMemberid())
            .postOwnerName(postOwner.getMembername())
            .postOwnerAvatar(postOwner.getMemberphoto() != null ? 
                Base64.getEncoder().encodeToString(postOwner.getMemberphoto()) : null)
            .collectedCount(collect.getCollectedcount())
            .build();
    }
}