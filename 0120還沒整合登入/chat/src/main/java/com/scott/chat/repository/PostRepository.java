package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scott.chat.model.Chatlog;
import com.scott.chat.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
	List<Post> findByMember_MemberidAndPostid(Integer memberid, Integer postid);
}