package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping("/volunteers/chat/rooms")
    public ResponseEntity<FindChatRoomsResponse> findChatRoomsByVolunteer(
        @LoginUser Long volunteerId) {
        FindChatRoomsResponse findChatRoomsResponse
            = chatService.findChatRoomsByVolunteer(volunteerId);
        return ResponseEntity.ok(findChatRoomsResponse);
    }
}
