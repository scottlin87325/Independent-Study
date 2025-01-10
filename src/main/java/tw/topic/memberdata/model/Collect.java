package tw.topic.memberdata.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Collect {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CollectID")
	private Long collectid;
	
	private Long postid;
	private Long collecterid;
	
	@Column(name = "Collected_count")
	private Long collectedcount;

	//---------------------------------------------------------------------
	public Long getCollectid() {
		return collectid;
	}
	public void setCollectid(Long collectid) {
		this.collectid = collectid;
	}
	public Long getPostid() {
		return postid;
	}
	public void setPostid(Long postid) {
		this.postid = postid;
	}
	public Long getCollecterid() {
		return collecterid;
	}
	public void setCollecterid(Long collecterid) {
		this.collecterid = collecterid;
	}
	public Long getCollectedcount() {
		return collectedcount;
	}
	public void setCollectedcount(Long collectedcount) {
		this.collectedcount = collectedcount;
	}
	
}