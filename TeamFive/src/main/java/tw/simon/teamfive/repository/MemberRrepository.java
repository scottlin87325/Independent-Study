package tw.simon.teamfive.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.simon.teamfive.model.Member;



@Repository
public interface MemberRrepository extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
}
