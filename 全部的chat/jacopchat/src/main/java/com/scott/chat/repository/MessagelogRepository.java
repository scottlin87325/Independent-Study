package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.Messagelog;
import java.util.List;

@Repository
public interface MessagelogRepository extends JpaRepository<Messagelog, Integer> {
    // 修改查詢，使用正確的屬性名稱
    @Query("SELECT m FROM Messagelog m WHERE m.messageid IN " +
           "(SELECT mb.messagelogid FROM MessageBoard mb WHERE mb.postid = :postId)")
    List<Messagelog> findByPostId(@Param("postId") Integer postId, Pageable pageable);
}