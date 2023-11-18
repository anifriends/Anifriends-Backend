package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.controller.ChatMessageResponse;
import com.clova.anifriends.domain.chat.dto.response.NewChatMessageResponse;
import com.clova.anifriends.domain.chat.exception.ChatRoomNotFoundException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public NewChatMessageResponse registerNewChatMessage(
        Long chatRoomId,
        Long senderId,
        UserRole senderRole,
        String message
    ) {
        ChatMessage chatMessage = saveChatMessage(chatRoomId, senderId, senderRole, message);
        return NewChatMessageResponse.from(chatMessage);
    }

    public ChatMessageResponse registerChatMessage(
        Long chatRoomId,
        Long senderId,
        UserRole senderRole,
        String message
    ) {
        ChatMessage chatMessage = saveChatMessage(chatRoomId, senderId, senderRole, message);
        return ChatMessageResponse.from(chatMessage);
    }

    private ChatMessage saveChatMessage(
        Long chatRoomId,
        Long senderId,
        UserRole senderRole,
        String message
    ) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        ChatMessage chatMessage = new ChatMessage(chatRoom, senderId, senderRole, message);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));
    }
}
