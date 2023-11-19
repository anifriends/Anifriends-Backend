package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.chat.dto.request.ChatMessageRequest;
import com.clova.anifriends.domain.chat.dto.response.NewChatMessageResponse;
import com.clova.anifriends.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @MessageMapping("/new/chat/rooms/{chatRoomId}/shelters/{shelterId}")
    @SendTo("/sub/new/chat/rooms/shelters/{shelterId}")
    public NewChatMessageResponse newChatMessage(
        @DestinationVariable Long chatRoomId,
        @Payload ChatMessageRequest chatMessageResponse
    ) {
        return chatMessageService.registerNewChatMessage(
            chatRoomId, chatMessageResponse.chatSenderId(), chatMessageResponse.chatSenderRole(),
            chatMessageResponse.chatMessage());
    }

    @MessageMapping("/chat/rooms/{chatRoomId}")
    @SendTo("/sub/chat/rooms/{chatRoomId}")
    public ChatMessageResponse chatMessage(
        @DestinationVariable Long chatRoomId,
        @Payload ChatMessageRequest chatMessageResponse
    ) {
        return chatMessageService.registerChatMessage(
            chatRoomId, chatMessageResponse.chatSenderId(), chatMessageResponse.chatSenderRole(),
            chatMessageResponse.chatMessage()
        );
    }
}
