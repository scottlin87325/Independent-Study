package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.Chatroom;
import java.util.List;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    List<Chatroom> findByMemberaOrMemberb(Long membera, Long memberb);
}