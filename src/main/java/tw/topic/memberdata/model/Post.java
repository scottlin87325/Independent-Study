package tw.topic.memberdata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PostID")
	private Long postid;

	private Long posterid;
	
	@Column(name = "Post_time")
	private String posttime;
	@Column(name = "Liked_count")
	private Long likedcount;
	@Column(name = "Message_count")
	private Long messagecount;
	@Column(name = "Post_content")
	private Long postcontent;
	
	//---------------------------------------------------------------------
	public Long getPostid() {
		return postid;
	}
	public void setPostid(Long postid) {
		this.postid = postid;
	}
	public Long getPosterid() {
		return posterid;
	}
	public void setPosterid(Long posterid) {
		this.posterid = posterid;
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
	public Long getPostcontent() {
		return postcontent;
	}
	public void setPostcontent(Long postcontent) {
		this.postcontent = postcontent;
	}
	
}