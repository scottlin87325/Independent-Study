package com.scott.chat.model;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Chatlog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ChatlogID")
	private Long chatlogid;
	
	private Long chatroomid;
	private Long senderid;
	
	@Column(name = "Input_time")
	private String inputtime;
	@Column(name = "Room_message")
	private String roommessage;
	
	private Long stickerid;
	
	// file傳輸三階段變數
	@Column(name = "Room_file")
	private byte[] roomfile;	// 以下兩個階段變數過長，roomfile縮寫成rf
	@Transient
	private MultipartFile roomfilefile;
	@Transient
	private String roomfilebase64;
	
	//---------------------------------------------------------------------
	public Long getChatlogid() {
		return chatlogid;
	}
	public void setChatlogid(Long chatlogid) {
		this.chatlogid = chatlogid;
	}
	public Long getChatroomid() {
		return chatroomid;
	}
	public void setChatroomid(Long chatroomid) {
		this.chatroomid = chatroomid;
	}
	public Long getSenderid() {
		return senderid;
	}
	public void setSenderid(Long senderid) {
		this.senderid = senderid;
	}
	public String getInputtime() {
		return inputtime;
	}
	public void setInputtime(String inputtime) {
		this.inputtime = inputtime;
	}
	public String getRoommessage() {
		return roommessage;
	}
	public void setRoommessage(String roommessage) {
		this.roommessage = roommessage;
	}
	public Long getStickerid() {
		return stickerid;
	}
	public void setStickerid(Long stickerid) {
		this.stickerid = stickerid;
	}
	
	//---------------------------------------------------------------------
	public byte[] getRoomfile() {
		return roomfile;
	}
	public void setRoomfile(byte[] roomfile) {
		System.out.println("上傳聊天室檔案");
		this.roomfile = roomfile;
		try {
			roomfile = roomfilefile.getBytes();
		} catch (IOException e) {
		}
	}
	public MultipartFile getRoomfilefile() {
		return roomfilefile;
	}
	public void setRoomfilefile(MultipartFile roomfilefile) {
		this.roomfilefile = roomfilefile;
	}
	public String getRoomfilebase64() {
		return roomfilebase64;
	}
	public void setRoomfilebase64(String roomfilebase64) {
		this.roomfilebase64 = roomfilebase64;
	}

}