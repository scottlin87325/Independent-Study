package com.scott.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scott.chat.model.Member;
import com.scott.chat.model.Post;
import com.scott.chat.model.PostPhoto;
import com.scott.chat.service.PostService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/messageborad")
public class MessageBoardController {
	@Autowired
	private PostService postService;
	
	@GetMapping("/post/{memberid}/{postid}")
	public String getPost(@PathVariable Integer memberid,@PathVariable Integer postid ,HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
            model.addAttribute("member", member);
        }
		List<Post> posts = postService.getPostsByMemberIdAndPostId(memberid, postid);

		for (Post post : posts) {
            String postTimeFromDb = post.getPosttime();
            byte[] postMemberPhotoFromDb = post.getMember().getMemberphoto();
            post.getMember().setMemberphoto(postMemberPhotoFromDb);
//            System.out.println(post.getMember().getMemberphotobase64());
            // 假設要將空格替換為 T
            String formattedPostTime = postTimeFromDb.replace(" ", "T");
            post.setPosttime(formattedPostTime); // 更新 posttime
            
            //有多張照片，處理 postPhotos
            List<PostPhoto> postPhotos = post.getPostphoto(); // 獲取與帖子關聯的所有照片
            for (PostPhoto photo : postPhotos) {
                // 可以對每張照片進行進一步處理
                // 例如將照片轉為 Base64 編碼等
            	photo.setPostedphoto(photo.getPostedphoto());
            }
        }
        model.addAttribute("posts", posts);
        return "messageboard";
	}
}
