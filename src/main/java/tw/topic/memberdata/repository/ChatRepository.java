package tw.topic.memberdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.topic.memberdata.model.Chatlog;

@Repository
public interface ChatRepository extends JpaRepository<Chatlog, Long>{

}