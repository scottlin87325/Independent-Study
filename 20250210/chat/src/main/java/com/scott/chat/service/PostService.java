package com.scott.chat.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scott.chat.model.Member;
import com.scott.chat.model.Post;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    public Post findById(Integer postId) {
        return postRepository.findById(postId).orElse(null);
    }
    
    @Transactional
    public Post createPost(Post post) {
        post.setPosttime(LocalDateTime.now().toString());
        post.setLikedcount(0);
        post.setMessagecount(0);
        Post savedPost = postRepository.save(post);
        
        Member member = memberRepository.findById(post.getPosterid()).orElse(null);
        if (member != null) {
            // 改用 countByPosterid 而不是 findByPosterid
            Integer actualPostCount = postRepository.countByPosterid(member.getMemberid());
            member.setPostcount(actualPostCount);
            memberRepository.save(member);
        }
        
        return savedPost;
    }
    
    public List<Post> findByPosterid(Integer posterId) {
        return postRepository.findByPosterid(posterId);
    }
    
    public List<Post> getAllPostsOrderByTime() {
        return postRepository.findAllOrderByPostTimeDesc();
    }
}