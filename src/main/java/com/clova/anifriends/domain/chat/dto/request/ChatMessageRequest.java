package com.clova.anifriends.domain.chat.dto.request;

import com.clova.anifriends.domain.auth.jwt.UserRole;

public record ChatMessageRequest(
    Long chatSenderId,
    UserRole chatSenderRole,
    String chatMessage
) {

}
