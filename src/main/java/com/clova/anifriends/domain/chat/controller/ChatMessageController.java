package com.clova.anifriends.domain.chat.controller;

import com.clova.anifriends.domain.chat.message.pub.NewChatMessagePub;
import com.clova.anifriends.domain.chat.message.sub.ChatMessageSub;
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
    public NewChatMessagePub newChatMessage(
        @DestinationVariable Long chatRoomId,
        @Payload ChatMessageSub chatMessageSub
    ) {
        return chatMessageService.registerChatMessage(
            chatRoomId, chatMessageSub.chatSenderId(), chatMessageSub.chatSenderRole(),
            chatMessageSub.chatMessage());
    }

}
