package tw.simon.teamfive.model;

import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Member2 {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MemberID")
	private Long id;
	
	private String email;
	private String password;
	private String realname;
	private char gender;
	private String telephone;
	private String birthday;
	private String introduce;
	
	@Column(name="Member_name")
	private String membername;
	
	@Column(name="Member_photo")
	private byte[] memberphoto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public byte[] getMemberphoto() {
		return memberphoto;
	}

	public void setMemberphoto(byte[] memberphoto) {
		this.memberphoto = memberphoto;
	}
	
//	---------------------------------------------------
	
//	處理顯示的畫面
	@Transient
	private String iconBase64;
	
//	對應表單
	@Transient
	private MultipartFile iconFile;

	public String getIconBase64() {
		return iconBase64;
	}

	public void setIconBase64(String iconBase64) {
		this.iconBase64 = iconBase64;
	}

	public MultipartFile getIconFile() {
		return iconFile;
	}

	public void setIconFile(MultipartFile iconFile) {
		this.iconFile = iconFile;
		try {
			memberphoto = iconFile.getBytes();  //給 orm 存 blob
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
