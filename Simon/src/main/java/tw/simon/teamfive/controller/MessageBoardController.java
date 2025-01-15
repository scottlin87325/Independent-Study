package tw.simon.teamfive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import tw.simon.teamfive.model.Member;
import tw.simon.teamfive.model.Post;
import tw.simon.teamfive.service.PostService;

@Controller
@RequestMapping("/messageborad")
public class MessageBoardController {
	@Autowired
	private PostService postService;
	
	@GetMapping("/post/{memberid}/{postid}")
	public String getPost(@PathVariable Long memberid,@PathVariable Long postid ,HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("member");
		if (member != null) {
            model.addAttribute("member", member);
        }
		List<Post> posts = postService.getPostsByMemberIdAndPostId(memberid, postid);
//		@Query("SELECT p FROM Post p JOIN FETCH p.member WHERE p.member.id = :memberid AND p.id = :postid")
//		List<Post> getPostsByMemberIdAndPostId(@Param("memberid") Long memberid, @Param("postid") Long postid);

		for (Post post : posts) {
            String postTimeFromDb = post.getPosttime();
            byte[] postPhotoFromDb = post.getMember().getMemberphoto();
            post.getMember().setMemberphoto(postPhotoFromDb);
            // 假設要將空格替換為 T
            String formattedPostTime = postTimeFromDb.replace(" ", "T");
            post.setPosttime(formattedPostTime); // 更新 posttime
        }
        model.addAttribute("posts", posts);
        return "message_board";
	}
}
