package tw.simon.teamfive.model;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PostID")
	private Long Id;
	
	@Column(name = "Post_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String posttime;
	@Column(name = "Liked_count")
	private Long likedcount;
	@Column(name = "Message_count")
	private Long messagecount;
	@Column(name = "Post_count")
	private Long postcount;
	@Column(name = "Post_content")
	private String postcontent;
	
	//---------------------------------------------------------------------
	public Long getId() {
		return Id;
	}
	public void setId(Long Id) {
		this.Id = Id;
	}
	public String getPosttime() {
		return posttime;
	}
	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}
	public Long getLikedcount() {
		return likedcount;
	}
	public void setLikedcount(Long likedcount) {
		this.likedcount = likedcount;
	}
	public Long getMessagecount() {
		return messagecount;
	}
	public void setMessagecount(Long messagecount) {
		this.messagecount = messagecount;
	}
	public Long getPostcount() {
		return postcount;
	}
	public void setPostcount(Long postcount) {
		this.postcount = postcount;
	}
	public String getPostcontent() {
		return postcontent;
	}
	public void setPostcontent(String postcontent) {
		this.postcontent = postcontent;
	}
	
//	-------------------------------以下JOIN----------------------------------
	
	@ManyToOne
	@JoinColumn(name="PosterID")
	private Member member;

	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	//---
	@OneToMany(mappedBy = "post")
	private List<MessageBoard> messageBoards;

	public List<MessageBoard> getMessageBoards() {
		return messageBoards;
	}
	public void setMessageBoards(List<MessageBoard> messageBoards) {
		this.messageBoards = messageBoards;
	}
	
	
}