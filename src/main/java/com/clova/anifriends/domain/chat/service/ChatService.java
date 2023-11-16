package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.chat.ChatRoom;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.exception.ChatNotFoundException;
import com.clova.anifriends.domain.chat.repository.ChatMessageRepository;
import com.clova.anifriends.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional(readOnly = true)
    public FindChatRoomDetailResponse findChatRoomDetailByVolunteer(Long chatRoomId) {
        ChatRoom chatRoom = getChatRoomWithShelter(chatRoomId);
        chatMessageRepository.readPartnerMessages(chatRoom, UserRole.ROLE_VOLUNTEER);
        return FindChatRoomDetailResponse.fromVolunteer(chatRoom);
    }

    private ChatRoom getChatRoomWithShelter(Long chatRoomId) {
        return chatRoomRepository.findByIdWithShelter(chatRoomId)
            .orElseThrow(() -> new ChatNotFoundException("존재하지 않는 채팅방입니다."));
    }
}
