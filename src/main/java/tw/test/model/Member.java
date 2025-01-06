package tw.test.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MemberID")
    private Long memberid;
    
    private String email;
    private String password;
    private String realname;
    
    @Column(name = "Member_name")
	private String membername;
    
    @Column(name = "Member_photo")
    private byte[] memberphoto;
	@Transient
	private MultipartFile memberphotofile;
	@Transient
	private String memberphotobase64;
    
    private String gender;
    
    private String telephone;

    private String birthday;
    
    private String introduce;
        
    ///////////////////////
	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
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

/////////////////////////////////////
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
		
    /////////////////////////////////////
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
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

   
   
}
	
	

	
	
	

