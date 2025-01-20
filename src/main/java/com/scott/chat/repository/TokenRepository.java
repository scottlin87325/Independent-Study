	package com.scott.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scott.chat.model.TokenRecord;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenRecord, Long> {

    // 根據 token 查找 TokenRecord
	  @Query("SELECT t FROM TokenRecord t WHERE t.token = :token")
	    Optional<TokenRecord> findByToken(@Param("token") String token);

    // 根據 member_id 查找未過期的 Token
    Optional<TokenRecord> findByMember_memberidAndExpiryDateAfter(Long memberid, Date expiryDate);
    
    // 刪除過期的 token
    long deleteByExpiryDateBefore(java.util.Date currentDate);
}

