package com.scott.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scott.chat.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

	List<Post> findByMember_MemberidAndPostid(Integer memberid, Integer postid);
	
}
