package com.scott.chat.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Chatroom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ChatroomID")
	private Long chatroomid;

	@Column(name = "Member_a")
	private Long membera;
	@Column(name = "Member_b")
	private Long memberb;
	
	//---------------------------------------------------------------------
	public Long getChatroomid() {
		return chatroomid;
	}
	public void setChatroomid(Long chatroomid) {
		this.chatroomid = chatroomid;
	}
	public Long getMembera() {
		return membera;
	}
	public void setMembera(Long membera) {
		this.membera = membera;
	}
	public Long getMemberb() {
		return memberb;
	}
	public void setMemberb(Long memberb) {
		this.memberb = memberb;
	}
	
}