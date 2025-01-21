package com.scott.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import com.scott.chat.model.Chatlog;
import com.scott.chat.model.Chatroom;
import com.scott.chat.model.ChatroomDTO;
import com.scott.chat.model.Member;
import com.scott.chat.service.ChatService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<Chatroom> createChatroom(
            @RequestParam Integer membera,
            @RequestParam Integer memberb) {
        return ResponseEntity.ok(chatService.createChatroom(membera, memberb));
    }

    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<ChatroomDTO>> getUserChatrooms(@PathVariable Integer userId) {
        return ResponseEntity.ok(chatService.getUserChatrooms(userId));
    }

    @GetMapping("/history/{chatroomId}")
    public ResponseEntity<List<Chatlog>> getChatHistory(
            @PathVariable Integer chatroomId) {
        return ResponseEntity.ok(chatService.getChatHistory(chatroomId));
    }

    @MessageMapping("/chat/{chatroomId}")
    @SendTo("/topic/messages/{chatroomId}")
    public Chatlog handleMessage(@DestinationVariable Integer chatroomId, Chatlog message) {
        log.info("收到WebSocket訊息: chatroomId={}, message={}", chatroomId, message);
        try {
            Chatlog savedMessage = chatService.saveMessage(message);
            log.info("訊息儲存成功: {}", savedMessage);
            return savedMessage;
        } catch (Exception e) {
            log.error("處理訊息時發生錯誤", e);
            throw e;
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Integer>> getCurrentUser(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("userId", member.getMemberid());
        return ResponseEntity.ok(response);
    }
}