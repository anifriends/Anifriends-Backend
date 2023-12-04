package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.ChatMessageResponse;
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

    public ChatMessageResponse registerChatMessage(
        Long chatRoomId,
        Long senderId,
        UserRole senderRole,
        String message
    ) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        ChatMessage chatMessage = new ChatMessage(chatRoom, senderId, senderRole, message);
        ChatMessage persistChatMessage = chatMessageRepository.save(chatMessage);

        return ChatMessageResponse.from(persistChatMessage);
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException("존재 하지 않는 채팅 방입니다."));
    }
}
