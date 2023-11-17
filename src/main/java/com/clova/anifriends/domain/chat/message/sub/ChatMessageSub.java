package com.clova.anifriends.domain.chat.message.sub;

import com.clova.anifriends.domain.auth.jwt.UserRole;

public record ChatMessageSub(
    Long chatSenderId,
    UserRole chatSenderRole,
    String chatMessage
) {

}
