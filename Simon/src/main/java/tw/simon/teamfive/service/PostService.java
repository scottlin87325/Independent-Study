package tw.simon.teamfive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.simon.teamfive.model.Post;
import tw.simon.teamfive.repository.PostRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	public List<Post> getPostsByMemberIdAndPostId(Long memberid, Long postid) {
        return postRepository.findByMemberIdAndId(memberid, postid);
    }
	
}
