package tw.brad.stest5.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.brad.stest5.model.Member;
import tw.brad.stest5.repository.MemberRrepository;
import tw.brad.stest5.util.BCrypt;

@Service
public class MemberService {
	@Autowired
	private MemberRrepository memberRrepository;
	
	public void addMember(Member member) {
		member.setPasswd(BCrypt.hashpw(member.getPasswd(), BCrypt.gensalt()));
		memberRrepository.save(member);
		System.out.println("bug1 : " + member.getIcon().length);
		System.out.println("bug2 : " + member.getIconBase64());
	}
	
	public Member loginMember(Member loginMember) {
		Optional<Member> opt = memberRrepository.findByAccount(loginMember.getAccount());
		Member member = opt.get();
		if(member !=null) {
			if(!BCrypt.checkpw(loginMember.getPasswd(), member.getPasswd())) {
				member = null;
			}else {
				member.setIconBase64(Base64.getEncoder().encodeToString(member.getIcon()));
			}
		}
		return member;
	}
}
