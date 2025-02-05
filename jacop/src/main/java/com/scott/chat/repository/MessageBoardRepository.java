package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.MessageBoard;
import java.util.Optional;

@Repository
public interface MessageBoardRepository extends JpaRepository<MessageBoard, Integer> {
    Optional<MessageBoard> findByPostid(Integer postId);
}