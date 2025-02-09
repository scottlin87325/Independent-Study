package com.scott.chat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketHandler {
	private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // 發送變動 ID 的消息
    public void sendMessageToAll(Integer id) {
        messagingTemplate.convertAndSend("/topic/message", id);  // 這裡發送的是 id
    }
}
