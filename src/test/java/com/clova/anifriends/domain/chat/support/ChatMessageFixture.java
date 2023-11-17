package com.clova.anifriends.domain.chat.support;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;

public class ChatMessageFixture {

    public static final String CHAT_MESSAGE = "chatMessage";

    public static ChatMessage chatMessage(ChatRoom chatRoom, Long senderId, UserRole senderRole) {
        return new ChatMessage(chatRoom, senderId, senderRole, CHAT_MESSAGE);
    }

}
