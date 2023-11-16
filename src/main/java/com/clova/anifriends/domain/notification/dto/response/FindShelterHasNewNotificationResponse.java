package com.clova.anifriends.domain.notification.dto.response;

public record FindShelterHasNewNotificationResponse (
    boolean hasNewNotification
) {

    public static FindShelterHasNewNotificationResponse from (boolean hasNewNotification) {
        return new FindShelterHasNewNotificationResponse(hasNewNotification);
    }
}
