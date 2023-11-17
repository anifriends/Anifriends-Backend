package com.clova.anifriends.domain.chat.repository.response;

import java.time.LocalDateTime;

public interface FindChatRoomResult {

    Long getChatRoomId();
    String getChatRecentMessage();
    String getChatPartnerName();
    String getChatPartnerImageUrl();
    LocalDateTime getCreatedAt();
    Long getChatUnReadCount();
}
