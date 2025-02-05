package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.PostPhoto;
import java.util.List;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Integer> {
    // 根據貼文ID查詢所有相關圖片
    List<PostPhoto> findByPostid(Integer postId);
    // 根據貼文ID刪除所有相關圖片
    void deleteByPostid(Integer postId);
    // 計算特定貼文的圖片數量
    long countByPostid(Integer postId);
}