package tw.test.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.test.model.Member;
import tw.test.repository.MemberRepository;
import tw.test.util.BCrypt;

@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;
	
	//新增會員
	public void addMember(Member member) {
		member.setPassword(BCrypt.hashpw(member.getPassword(), BCrypt.gensalt()));
		memberRepository.save(member);
	}
	
	/*
	public Member loginMember(Member loginMember) {
		Optional<Member> opt = memberRepository.findByAccount(loginMember.getAccount());
		Member member = opt.get();
		if (member != null) {
			if (!BCrypt.checkpw(loginMember.getPasswd(), member.getPasswd())) {
				member = null;
			}
		}
		return member;
	}*/
	
	//根據帳號查詢會員
	public Member findMemberByAccount(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
}
