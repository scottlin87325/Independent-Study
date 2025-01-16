package tw.simon.teamfive.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.simon.teamfive.model.Member;
import tw.simon.teamfive.repository.MemberRepository;
import tw.simon.teamfive.util.BCrypt;

@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;
	
	public void addMember(Member member) {
		member.setPassword(BCrypt.hashpw(member.getPassword(), BCrypt.gensalt()));
		memberRepository.save(member);
//		System.out.println("bug1 : " + member.getMemberphoto().length);
//		System.out.println("bug2 : " + member.getIconBase64());
	}
	
	public Member loginMember(Member loginMember) {
		Optional<Member> opt = memberRepository.findByEmail(loginMember.getEmail());
		Member member = opt.get();
		if(member !=null) {
			if(!BCrypt.checkpw(loginMember.getPassword(), member.getPassword())) {
				member = null;
			}else {
				member.setMemberphotobase64(Base64.getEncoder().encodeToString(member.getMemberphoto()));
			}
		}
		return member;
	}
}
