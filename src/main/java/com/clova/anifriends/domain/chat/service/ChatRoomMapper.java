package com.clova.anifriends.domain.chat.service;

import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse;
import com.clova.anifriends.domain.chat.dto.response.FindChatRoomsResponse.FindChatRoomResponse;
import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatRoomMapper {

    public static FindChatRoomsResponse resultToResponse(
        List<FindChatRoomResult> findChatRoomsResult) {
        List<FindChatRoomResponse> chatRooms = findChatRoomsResult.stream()
            .map(chatRoom -> new FindChatRoomResponse(
                chatRoom.getChatRoomId(),
                chatRoom.getChatRecentMessage(),
                chatRoom.getChatPartnerName(),
                chatRoom.getChatPartnerImageUrl(),
                chatRoom.getCreatedAt(),
                chatRoom.getChatUnReadCount()
            ))
            .toList();
        return new FindChatRoomsResponse(chatRooms);
    }
}
