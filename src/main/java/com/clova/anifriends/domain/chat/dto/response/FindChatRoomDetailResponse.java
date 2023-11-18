package com.clova.anifriends.domain.chat.dto.response;

import com.clova.anifriends.domain.chat.ChatRoom;

public record FindChatRoomDetailResponse(
    String chatPartnerImageUrl,
    String chatPartnerName) {

    public static FindChatRoomDetailResponse fromVolunteer(ChatRoom chatRoom) {
        return new FindChatRoomDetailResponse(
            chatRoom.getShelter().getImage(),
            chatRoom.getShelter().getName());
    }

    public static FindChatRoomDetailResponse fromShelter(ChatRoom chatRoom) {
        return new FindChatRoomDetailResponse(
            chatRoom.getVolunteer().getVolunteerImageUrl(),
            chatRoom.getVolunteer().getName());
    }
}
