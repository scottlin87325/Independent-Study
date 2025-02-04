package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.Post;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    // 查詢特定用戶的所有貼文
    List<Post> findByPosterid(Integer posterId);
    // 分頁查詢所有貼文
    Page<Post> findAll(Pageable pageable);
    // 分頁查詢特定用戶的貼文
    Page<Post> findByPosteridOrderByPosttimeDesc(Integer posterId, Pageable pageable);
    // 計算特定用戶的貼文總數
    Long countByPosterid(Integer posterId);
}