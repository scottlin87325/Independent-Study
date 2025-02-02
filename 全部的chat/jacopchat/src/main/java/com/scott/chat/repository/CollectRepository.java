package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.Collect;

@Repository
public interface CollectRepository extends JpaRepository<Collect, Integer> {
    // 分頁查詢特定用戶的收藏列表
    Page<Collect> findByCollecterid(Integer collecterId, Pageable pageable);
    // 檢查特定用戶是否已收藏某貼文
    boolean existsByPostidAndCollecterid(Integer postId, Integer collecterId);
    // 刪除特定用戶對某貼文的收藏
    void deleteByPostidAndCollecterid(Integer postId, Integer collecterId);
    // 計算某貼文的收藏總數
    int countByPostid(Integer postId);
}