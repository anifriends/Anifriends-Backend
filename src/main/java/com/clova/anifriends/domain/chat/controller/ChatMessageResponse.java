package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import java.time.LocalDateTime;

public record ChatMessageResponse(
    Long chatSenderId,
    UserRole chatSenderRole,
    String chatMessage,
    LocalDateTime createdAt
) {

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return new ChatMessageResponse(
            chatMessage.getSenderId(),
            chatMessage.getSenderRole(),
            chatMessage.getMessage(),
            chatMessage.getCreatedAt()
        );
    }
}
