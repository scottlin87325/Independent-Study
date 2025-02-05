package com.scott.chat.repository;

import com.scott.chat.model.Post;
import com.scott.chat.model.PostLike;
import com.scott.chat.model.Member;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    
    // 檢查特定會員是否已對貼文按讚
    boolean existsByPostAndMember(Post post, Member member);
    
    // 查找特定會員對特定貼文的按讚記錄
    Optional<PostLike> findByPostAndMember(Post post, Member member);
    
    // 計算特定貼文的按讚數量
    long countByPost(Post post);
    
    // 刪除特定會員對特定貼文的按讚
    void deleteByPostAndMember(Post post, Member member);
    
    // 查詢特定貼文最近的按讚記錄
    @Query("SELECT pl FROM PostLike pl WHERE pl.post = :post ORDER BY pl.createdAt DESC")
    List<PostLike> findRecentLikesByPost(@Param("post") Post post, Pageable pageable);
}