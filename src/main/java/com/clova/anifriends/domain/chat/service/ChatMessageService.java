package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatMessage;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.exception.ChatRoomNotFoundException;
import com.clova.anifriends.domain.chat.message.pub.NewChatMessagePub;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public NewChatMessagePub registerChatMessage(
        Long chatRoomId,
        Long senderId,
        UserRole senderRole,
        String message
    ) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        ChatMessage chatMessage = new ChatMessage(chatRoom, senderId, senderRole, message);
        chatMessageRepository.save(chatMessage);

        return NewChatMessagePub.from(chatMessage);
    }

    private ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));
    }
}
