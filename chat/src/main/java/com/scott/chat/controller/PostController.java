package com.scott.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scott.chat.model.Post;
import com.scott.chat.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/Home")
public class PostController {
    
	@Autowired
	private PostService postService;
	
	@GetMapping("/post/{memberid}/{postid}")
	public String getPost(@PathVariable Integer memberid,@PathVariable Integer postid ,HttpSession session, Model model) {
		List<Post> posts = postService.getPostsByMemberIdAndPostId(memberid, postid);
		
		for (Post post : posts) {
            String postTimeFromDb = post.getPosttime();
            // 假設要將空格替換為 T
            String formattedPostTime = postTimeFromDb.replace(" ", "T");
            post.setPosttime(formattedPostTime); // 更新 posttime
            
            byte[] postPhotoFromDb = post.getMember().getMemberphoto();
            System.out.println(postPhotoFromDb);
            post.getMember().setMemberphoto(postPhotoFromDb);
            System.out.println(post.getMember().getMemberphotobase64());
            
            
        }
		
		model.addAttribute("posts", posts);
		return "/Home/post";
	}
	
}
