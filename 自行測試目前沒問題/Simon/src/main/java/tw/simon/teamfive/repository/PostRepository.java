package tw.simon.teamfive.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.simon.teamfive.model.Member;
import tw.simon.teamfive.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

	List<Post> findByMemberIdAndId(Long memberid, Long postid);
	
}
