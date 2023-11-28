package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.chat.dto.request.ChatMessageRequest;
import com.clova.anifriends.domain.chat.service.ChatMessageService;
import com.clova.anifriends.domain.chat.service.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final MessagePublisher messagePublisher;

    @MessageMapping("/chat/new/rooms/{chatRoomId}/shelters/{shelterId}")
    public void chatMessageOfNewRoom(
        @DestinationVariable Long chatRoomId,
        @DestinationVariable Long shelterId,
        @Payload ChatMessageRequest chatMessageResponse
    ) {
        ChatMessageResponse response = chatMessageService.registerChatMessage(
            chatRoomId, chatMessageResponse.chatSenderId(), chatMessageResponse.chatSenderRole(),
            chatMessageResponse.chatMessage());
        messagePublisher.publish("/sub/chat/new/rooms/shelters/" + shelterId, response);
    }

    @MessageMapping("/chat/rooms/{chatRoomId}")
    public void chatMessage(
        @DestinationVariable Long chatRoomId,
        @Payload ChatMessageRequest chatMessageResponse
    ) {
        ChatMessageResponse response = chatMessageService.registerChatMessage(
            chatRoomId, chatMessageResponse.chatSenderId(), chatMessageResponse.chatSenderRole(),
            chatMessageResponse.chatMessage());
        messagePublisher.publish("/sub/chat/rooms/" + chatRoomId, response);
    }
}
