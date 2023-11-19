package com.clova.anifriends.domain.chat.dto.response;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import java.time.LocalDateTime;

public record NewChatMessageResponse(
    Long chatRoomId,
    NewChatMessage chatMessage
) {

    public static NewChatMessageResponse from(ChatMessage chatMessage) {
        return new NewChatMessageResponse(
            chatMessage.getChatRoom().getChatRoomId(),
            new NewChatMessage(
                chatMessage.getSenderId(),
                chatMessage.getSenderRole(),
                chatMessage.getMessage(),
                chatMessage.getCreatedAt()
            )
        );
    }

    public record NewChatMessage(
        Long chatSenderId,
        UserRole chatSenderRole,
        String chatMessage,
        LocalDateTime createdAt
    ) {

    }
}
