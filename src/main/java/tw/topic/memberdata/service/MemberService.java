package tw.topic.memberdata.service;

import org.springframework.web.multipart.MultipartFile;

import tw.topic.memberdata.model.Member;

public interface MemberService {
	public Member isAccountExist(String email);	 // 檢查是否唯一
	public Member register(Member membername, MultipartFile memberphoto);
	public boolean update(Member membername, MultipartFile memberphoto);
	public Member login(Member member);
}