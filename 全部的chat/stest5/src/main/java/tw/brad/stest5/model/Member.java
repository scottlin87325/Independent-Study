package tw.brad.stest5.model;

import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String account;
	private String passwd;
	private String realname;
	
//	對應資料表
	private byte[] icon;
	
//	處理顯示的畫面
	@Transient
	private String iconBase64;
	
	//	對應表單
	@Transient
	private MultipartFile iconFile;
	
	public MultipartFile getIconFile() {
		return iconFile;
	}
	public void setIconFile(MultipartFile iconFile){
		//上傳檔案
		this.iconFile = iconFile;
		try {
			icon = iconFile.getBytes();  //給 orm 存 blob
		}catch(Exception e){
			System.out.println(e);
		}
	}
	public byte[] getIcon() {
		return icon;
	}
	public void setIcon(byte[] icon) {
		//取回資料
		this.icon = icon;
//		System.out.println(iconBase64);
	}
	public String getIconBase64() {
		return iconBase64;
	}
	public void setIconBase64(String iconBase64) {
		this.iconBase64 = iconBase64;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
}
