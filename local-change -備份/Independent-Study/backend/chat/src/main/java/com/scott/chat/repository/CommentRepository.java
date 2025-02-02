package com.scott.chat.repository;

import com.scott.chat.model.Comment;
import com.scott.chat.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
   // 查詢特定貼文的所有評論，按建立時間降序排序
   @Query("SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.createdAt DESC")
   List<Comment> findByPostOrderByCreatedAtDesc(@Param("post") Post post);
   // 計算特定貼文的評論數量
   long countByPost(Post post);
   // 查詢特定會員的所有評論
   @Query("SELECT c FROM Comment c WHERE c.member.memberid = :memberId") 
   List<Comment> findByMemberId(@Param("memberId") Long memberId);
    // 刪除特定貼文的所有評論
   void deleteByPost(Post post);
}