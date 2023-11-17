package com.clova.anifriends.domain.notification.dto.response;

public record FindVolunteerHasNewNotificationResponse (
    boolean hasNewNotification
) {

    public static FindVolunteerHasNewNotificationResponse from (boolean hasNewNotification) {
        return new FindVolunteerHasNewNotificationResponse(hasNewNotification);
    }
}
