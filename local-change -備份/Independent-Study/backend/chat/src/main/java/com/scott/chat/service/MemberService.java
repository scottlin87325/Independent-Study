package com.scott.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scott.chat.model.Member;
import com.scott.chat.repository.MemberRepository;
import com.scott.chat.repository.PostRepository;
import com.scott.chat.exception.UnauthorizedException;
// 會員服務類別 - 處理所有與會員相關的業務邏輯
@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository; // 會員資料訪問對象
    
    @Autowired
    private PostRepository postRepository;  // 貼文資料訪問對象
        
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
        // 新增會員
    // 檢查email是否已存在，若存在則拋出異常
    // 對密碼進行加密處理
    // 設置初始發文數為0
    @Transactional
    public void addMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new RuntimeException("Email已被註冊");
        }
        
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        if (member.getPostcount() == null) {
            member.setPostcount(0);
        }
        memberRepository.save(member);
    }
    // 驗證密碼是否正確
    // 比對原始密碼與加密後的密碼
    @Transactional(readOnly = true)
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
     // 根據email查找會員
    @Transactional(readOnly = true)
    public Member findMemberByAccount(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }
    // 更新會員密碼
    // 對新密碼進行加密處理後儲存
    @Transactional
    public void updatePassword(Member member, String newPassword) {
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
  // 根據會員ID查找會員
    // 若找不到則拋出異常
    @Transactional(readOnly = true)
    public Member getMemberById(Integer memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("找不到用戶"));
    }
    // 更新會員資料
    // 只更新允許修改的欄位：會員名稱、照片、簡介
    @Transactional
    public void updateMember(Member member) {
        Member existingMember = memberRepository.findById(member.getMemberid())
            .orElseThrow(() -> new RuntimeException("找不到用戶"));
            
        // 只更新允許修改的欄位
        existingMember.setMembername(member.getMembername());
        existingMember.setMemberphoto(member.getMemberphoto());
        existingMember.setIntroduce(member.getIntroduce());
        
        memberRepository.save(existingMember);
    }
     // 檢查是否為貼文擁有者
    @Transactional(readOnly = true)
    public boolean isPostOwner(Integer memberId, Integer postId) {
        // 使用 PostRepository 來檢查貼文擁有者
        return postRepository.findById(postId)
            .map(post -> post.getPosterid().equals(memberId))
            .orElse(false);
    }
        // 更新會員發文數
    // isIncrement為true時增加發文數
    // isIncrement為false時減少發文數，最小值為0
    @Transactional
    public void updatePostCount(Integer memberId, boolean isIncrement) {
        Member member = getMemberById(memberId);
        if (isIncrement) {
            member.setPostcount(member.getPostcount() + 1);
        } else {
            member.setPostcount(Math.max(0, member.getPostcount() - 1));
        }
        memberRepository.save(member);
    }
}