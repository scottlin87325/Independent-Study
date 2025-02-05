package com.scott.chat.repository;

import com.scott.chat.model.Comment;
import com.scott.chat.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 評論資料存取層
* 負責處理評論相關的資料庫操作
*/
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

   /**
    * 查詢指定貼文的所有評論，依建立時間降序排序
    * @param post 目標貼文
    * @return 評論列表
    */
   @Query("SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.createdAt DESC")
   List<Comment> findByPostOrderByCreatedAtDesc(@Param("post") Post post);

   /**
    * 計算指定貼文的評論總數
    * @param post 目標貼文
    * @return 評論數量
    */
   long countByPost(Post post);

   /**
    * 查詢貼文的評論詳細資訊，包含會員資料
    * 返回格式: [評論ID, 會員ID, 評論內容, 建立時間, 會員名稱]
    * @param postId 貼文ID
    * @return 評論詳細資訊列表
    */
   @Query("SELECT c.id, m.memberid, c.content, c.createdAt, m.membername " +
          "FROM Comment c JOIN c.member m WHERE c.post.id = ?1 " +
          "ORDER BY c.createdAt DESC")
   List<Object[]> findCommentsWithMemberInfo(Integer postId);
}