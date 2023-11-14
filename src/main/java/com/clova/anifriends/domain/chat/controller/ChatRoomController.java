package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

}
