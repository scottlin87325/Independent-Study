package tw.brad.stest5.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.brad.stest5.model.Member;

@Repository
public interface MemberRrepository extends JpaRepository<Member, Long>{
	Optional<Member> findByAccount(String account);
}
