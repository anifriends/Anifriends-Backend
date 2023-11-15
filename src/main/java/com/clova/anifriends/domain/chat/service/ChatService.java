package com.clova.anifriends.domain.chat.service;

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
        ChatRoom chatRoom = chatRoomRepository.findByIdWithShelter(chatRoomId)
            .orElseThrow(() -> new ChatNotFoundException("존재하지 않는 채팅방입니다."));
        chatMessageRepository.readAllMessage(chatRoom);
        return FindChatRoomDetailResponse.fromVolunteer(chatRoom);
    }
}
