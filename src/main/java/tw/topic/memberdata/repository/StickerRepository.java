package tw.topic.memberdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.topic.memberdata.model.Sticker;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long>{
	
}