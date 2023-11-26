package com.clova.anifriends.domain.chat.dto.response;

import com.clova.anifriends.domain.chat.ChatRoom;

public record RegisterChatRoomResponse(Long chatRoomId) {

    public static RegisterChatRoomResponse from(ChatRoom chatRoom) {
        return new RegisterChatRoomResponse(chatRoom.getChatRoomId());
    }
}
