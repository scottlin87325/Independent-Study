package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.Chatlog;
import java.util.List;

@Repository
public interface ChatlogRepository extends JpaRepository<Chatlog, Long> {
    List<Chatlog> findByChatroomidOrderByInputtimeAsc(Long chatroomid);
}