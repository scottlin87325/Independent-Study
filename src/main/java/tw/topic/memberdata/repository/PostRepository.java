package tw.topic.memberdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.topic.memberdata.model.PostPhoto;

@Repository
public interface PostRepository extends JpaRepository<PostPhoto, Long>{

}