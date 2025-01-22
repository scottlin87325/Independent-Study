package com.scott.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scott.chat.model.Chatlog;
import com.scott.chat.model.Chatroom;
import com.scott.chat.repository.ChatlogRepository;
import com.scott.chat.repository.ChatroomRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    
    @Autowired
    private ChatroomRepository chatroomRepository;
    
    @Autowired
    private ChatlogRepository chatlogRepository;

    public Chatroom createChatroom(Integer membera, Integer memberb) {
        Chatroom room = new Chatroom();
        room.setMembera(membera);
        room.setMemberb(memberb);
        return chatroomRepository.save(room);
    }

    public List<Chatroom> getUserChatrooms(Integer userId) {
        return chatroomRepository.findByMemberaOrMemberb(userId, userId);
    }

    public Chatlog saveMessage(Chatlog message) {
        message.setInputtime(LocalDateTime.now().toString());
        return chatlogRepository.save(message);
    }

    public List<Chatlog> getChatHistory(Integer chatroomId) {
        return chatlogRepository.findByChatroom_ChatroomidOrderByInputtimeAsc(chatroomId);
    }
}