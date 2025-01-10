package tw.topic.memberdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.topic.memberdata.model.Messagelog;

@Repository
public interface MessageRepository extends JpaRepository<Messagelog, Long>{

}