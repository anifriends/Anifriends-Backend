package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.chat.dto.response.FindChatRoomDetailResponse;
import com.clova.anifriends.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping("/volunteers/chat/rooms/{chatRoomId}")
    public ResponseEntity<FindChatRoomDetailResponse> findChatRoomDetailByVolunteer(
        @PathVariable Long chatRoomId) {
        FindChatRoomDetailResponse findChatRoomDetailResponse
            = chatService.findChatRoomDetailByVolunteer(chatRoomId);
        return ResponseEntity.ok(findChatRoomDetailResponse);
    }
}
