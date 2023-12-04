package com.clova.anifriends.domain.chat.dto.response;

public record FindUnreadCountResponse(long totalUnreadCount) {

    public static FindUnreadCountResponse from(long unreadCount) {
        return new FindUnreadCountResponse(unreadCount);
    }
}
