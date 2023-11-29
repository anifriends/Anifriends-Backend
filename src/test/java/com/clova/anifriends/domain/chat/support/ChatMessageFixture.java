package com.clova.anifriends.domain.chat.support;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.volunteer.Volunteer;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class ChatMessageFixture {

    public static final String CHAT_MESSAGE = "chatMessage";

    public static ChatMessage chatMessage(ChatRoom chatRoom, Long senderId, UserRole senderRole) {
        ChatMessage chatMessage = new ChatMessage(chatRoom, senderId, senderRole, CHAT_MESSAGE);
        ReflectionTestUtils.setField(chatMessage, "createdAt", LocalDateTime.now());
        return chatMessage;
    }

    public static ChatMessage volunteerMessage(ChatRoom chatRoom, Volunteer volunteer) {
        return new ChatMessage(chatRoom, volunteer.getVolunteerId(), UserRole.ROLE_VOLUNTEER,
            CHAT_MESSAGE);
    }

    public static ChatMessage shelterMessage(ChatRoom chatRoom, Shelter shelter) {
        return new ChatMessage(chatRoom, shelter.getShelterId(), UserRole.ROLE_SHELTER,
            CHAT_MESSAGE);
    }
}
