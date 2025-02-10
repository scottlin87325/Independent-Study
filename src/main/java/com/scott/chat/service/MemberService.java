package com.scott.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scott.chat.model.Member;
import com.scott.chat.repository.MemberRepository;


@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;
		
	private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
	//新增會員
	public void addMember(Member member) {
		member.setPassword(passwordEncoder.encode(member.getPassword()));
		member.setPostcount(0); // 如果 Post_count 是 null，則設為 0
		member.setTelephone("090000000");
	    member.setBirthday("2000-01-01");
	    member.setGender("男");
		
		
		memberRepository.save(member);
	}
	
	 // 驗證密碼
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
	
	//根據帳號查詢會員
	public Member findMemberByAccount(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
	//更新密碼
	public void updatePassword(Member member, String newPassword) {
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
	
}
