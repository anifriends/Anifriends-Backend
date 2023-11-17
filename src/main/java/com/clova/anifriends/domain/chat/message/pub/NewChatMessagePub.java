package com.clova.anifriends.domain.chat.message.pub;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import java.time.LocalDateTime;

public record NewChatMessagePub(
    Long chatRoomId,
    NewChatMessage chatMessage
) {

    public static NewChatMessagePub from(ChatMessage chatMessage) {
        return new NewChatMessagePub(
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
