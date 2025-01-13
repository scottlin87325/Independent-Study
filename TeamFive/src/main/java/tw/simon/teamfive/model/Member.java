package tw.simon.teamfive.model;

import java.util.Base64;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MemberID")
	private Long id;
	
	private String email;	// TODO DB設為唯一鍵，JAVA這邊不知道有沒有問題
	private String password;	// TODO 加密
	private String realname;
	
	@Column(name = "Member_name")
	private String membername;
	
	// 大頭貼傳輸三階段變數
	@Column(name = "Member_photo")
	private byte[] memberphoto;
	@Transient
	private MultipartFile memberphotofile;
	@Transient
	private String memberphotobase64;
	
	private char gender;
	private String telephone;
	private String birthday;
	private String introduce;
	
//	@Column(name = "Post_count")
//	private Long postcount;
	
	//---------------------------------------------------------------------
	public Long getid() {
		return id;
	}
	public void setid(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getMembername() {
		return membername;
	}
	public void setMembername(String membername) {
		this.membername = membername;
	}
	
	//---------------------------------------------------------------------
	public byte[] getMemberphoto() {
		return memberphoto;
	}
	public void setMemberphoto(byte[] memberphoto) {
		System.out.println("取回大頭貼");
		this.memberphoto = memberphoto;
		memberphotobase64 = Base64.getEncoder().encodeToString(memberphoto);
		System.out.println(memberphotobase64);
	}
	public MultipartFile getMemberphotofile() {
		return memberphotofile;
	}
	public void setMemberphotofile(MultipartFile memberphotofile) {
		System.out.println("上傳大頭貼...");
		this.memberphotofile = memberphotofile;
		try {
			memberphoto = memberphotofile.getBytes();
		} catch (Exception e) {
		}
	}
	public String getMemberphotobase64() {
		return memberphotobase64;
	}
	public void setMemberphotobase64(String memberphotobase64) {
		this.memberphotobase64 = memberphotobase64;
	}
	
	//---------------------------------------------------------------------
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
//	public Long getPostcount() {
//		return postcount;
//	}
//	public void setPostcount(Long postcount) {
//		this.postcount = postcount;
//	}

//	-------------------------------以下JOIN----------------------------------
	
	@OneToMany(mappedBy = "member")
	private List<Post> post;

	public List<Post> getPost() {
		return post;
	}
	public void setPost(List<Post> post) {
		this.post = post;
	}
	
}
