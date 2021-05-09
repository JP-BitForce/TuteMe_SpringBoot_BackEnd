package com.bitforce.tuteme.controller;

import com.bitforce.tuteme.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/pubic")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/pubic")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/sendPrivateMessage")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiver().trim(), "/reply", chatMessage);
    }

    @MessageMapping("/addPrivateUser")
    @SendTo("/queue/reply")
    public ChatMessage addPrivateUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("private-username", chatMessage.getSender());
        return chatMessage;
    }
}
