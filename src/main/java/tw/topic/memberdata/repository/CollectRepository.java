package tw.topic.memberdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.topic.memberdata.model.Post;

@Repository
public interface CollectRepository extends JpaRepository<Post, Long>{

}