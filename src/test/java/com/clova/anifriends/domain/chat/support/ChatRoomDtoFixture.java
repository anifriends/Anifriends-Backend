package com.clova.anifriends.domain.chat.support;

import com.clova.anifriends.domain.chat.repository.response.FindChatRoomResult;
import java.time.LocalDateTime;

public class ChatRoomDtoFixture {

    public static FindChatRoomResult findChatRoomResult(
        Long chatRoomId, String chatRecentMessage, String chatPartnerName,
        String chatPartnerImageUrl, LocalDateTime createdAt, Long chatUnReadCount) {
        return new FindChatRoomResult() {
            @Override
            public Long getChatRoomId() {
                return chatRoomId;
            }

            @Override
            public String getChatRecentMessage() {
                return chatRecentMessage;
            }

            @Override
            public String getChatPartnerName() {
                return chatPartnerName;
            }

            @Override
            public String getChatPartnerImageUrl() {
                return chatPartnerImageUrl;
            }

            @Override
            public LocalDateTime getCreatedAt() {
                return createdAt;
            }

            @Override
            public Long getChatUnReadCount() {
                return chatUnReadCount;
            }
        };
    }
}
