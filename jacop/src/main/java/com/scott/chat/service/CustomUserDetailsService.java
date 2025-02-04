package com.scott.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scott.chat.model.CustomUserDetails;
import com.scott.chat.model.Member;
import com.scott.chat.repository.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private MemberRepository memberRepository;// 會員資料訪問對象
    // 根據用戶名(email)載入用戶詳情
    // 用於Spring Security的認證過程
    // 若找不到用戶則拋出異常
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("找不到用戶: " + email));
            
        return new CustomUserDetails(member);
    }
    // 獲取當前登入會員資料
    @Transactional(readOnly = true)
    public Member getCurrentMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("找不到用戶: " + email));
    }
    // 檢查email是否已被註冊
    @Transactional(readOnly = true)
    public boolean isEmailRegistered(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}