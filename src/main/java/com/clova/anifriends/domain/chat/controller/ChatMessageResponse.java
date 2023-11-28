package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;

public record ChatMessageResponse(
    Long chatRoomId,
    Long chatSenderId,
    UserRole chatSenderRole,
    String chatMessage,
    String createdAt
) {

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
            chatMessage.getChatRoom().getChatRoomId(),
            chatMessage.getSenderId(),
            chatMessage.getSenderRole(),
            chatMessage.getMessage(),
            chatMessage.getCreatedAt().toString()
        );
    }
}
