package com.scott.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scott.chat.model.Post;
import com.scott.chat.repository.PostRepository;

@Service
public class PostService {
    
	@Autowired
	private PostRepository postRepository;
	
    
//	撈帳號
	public List<Post> getPostsByMemberIdAndPostId(Integer memberid, Integer postid){
		return postRepository.findByMember_MemberidAndPostid(memberid, postid);
	}
	
}
