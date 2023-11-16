package com.clova.anifriends.domain.chat.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record FindChatRoomsResponse(List<FindChatRoomResponse> chatRooms) {

    public record FindChatRoomResponse(
        Long chatRoomId,
        String chatRecentMessage,
        String chatPartnerName,
        String charPartnerImageUrl,
        LocalDateTime createdAt,
        Long chatUnReadCount) {

    }
}
