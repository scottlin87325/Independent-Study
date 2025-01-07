package com.scott.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import com.scott.chat.model.Chatlog;
import com.scott.chat.model.Chatroom;
import com.scott.chat.service.ChatService;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/room")
    public ResponseEntity<Chatroom> createChatroom(
            @RequestParam Long membera,
            @RequestParam Long memberb) {
        return ResponseEntity.ok(chatService.createChatroom(membera, memberb));
    }

    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<Chatroom>> getUserChatrooms(
            @PathVariable Long userId) {
        return ResponseEntity.ok(chatService.getUserChatrooms(userId));
    }

    @GetMapping("/history/{chatroomId}")
    public ResponseEntity<List<Chatlog>> getChatHistory(
            @PathVariable Long chatroomId) {
        return ResponseEntity.ok(chatService.getChatHistory(chatroomId));
    }

    @MessageMapping("/chat/{chatroomId}")
    @SendTo("/topic/messages/{chatroomId}")
    public Chatlog handleMessage(@DestinationVariable Long chatroomId, Chatlog message) {
        return chatService.saveMessage(message);
    }
}