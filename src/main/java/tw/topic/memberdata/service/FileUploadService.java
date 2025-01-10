package tw.topic.memberdata.service;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.topic.memberdata.model.Member;
import tw.topic.memberdata.repository.ChatRepository;
import tw.topic.memberdata.repository.MemberRepository;
import tw.topic.memberdata.repository.MessageRepository;
import tw.topic.memberdata.repository.PostRepository;
import tw.topic.memberdata.repository.StickerRepository;
import tw.topic.memberdata.util.BCrypt;

@Service
public class FileUploadService {
	
	// 大頭貼
	@Autowired
	private MemberRepository memberRepository;
	// 使用者檔案上傳
	public void addMemberphoto(Member member) { // 這裡的member還是表單資料
		member.setPassword(BCrypt.hashpw(member.getPassword(), BCrypt.gensalt()));
		Member saveMemberphoto = memberRepository.save(member);
		System.out.println("debug1: " + member.getMemberphoto().length);
		System.out.println("debug2: " + member.getMemberphotobase64());
	}
	// 檔案從DB輸出到使用者端
	public Member loginMemberphoto(Member loginMember) {
		Optional<Member> opt = memberRepository.findByMemberid(loginMember.getMemberid());
		try {
			Member member = opt.get();
			if (member != null) { // 檢驗帳號
				if (!BCrypt.checkpw(loginMember.getPassword(), member.getPassword())) { // 檢驗密碼
					member = null;
				} else {
					member.setMemberphotobase64(Base64.getEncoder().encodeToString(member.getMemberphoto()));
					// 取得IconBase64的圖片
				}
			}
			return member;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Autowired
	private ChatRepository chatRepository;
	
	
	
	@Autowired
	private StickerRepository stickerRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private MessageRepository messageRepository;
	
}