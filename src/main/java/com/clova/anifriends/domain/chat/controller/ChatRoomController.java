package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.chat.dto.request.RegisterChatRoomRequest;
import com.clova.anifriends.domain.chat.service.ChatService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatRoomController {

    private final ChatService chatService;

    @PostMapping("/volunteers/chat/rooms")
    public ResponseEntity<Void> registerChatRoom(
        @LoginUser Long volunteerId,
        RegisterChatRoomRequest registerChatRoomRequest
    ) {
        long chatRoomId = chatService.registerChatRoom(volunteerId,
            registerChatRoomRequest.shelterId());

        URI location = URI.create("/api/volunteers/chat/rooms/" + chatRoomId);
        return ResponseEntity.created(location).build();
    }

}
