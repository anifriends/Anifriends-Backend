package com.clova.anifriends.domain.notification.dto.response;

import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import java.util.List;

public record FindVolunteerNotificationsResponse(
    List<FindVolunteerNotification> notifications
) {

    private record FindVolunteerNotification(
        Long notificationId,
        String notificationTitle,
        String notificationContent,
        Boolean notificationIsRead,
        NotificationType notificationType
    ) {

        private static FindVolunteerNotification from(VolunteerNotification volunteerNotification) {
            return new FindVolunteerNotification(
                volunteerNotification.getVolunteerNotificationId(),
                volunteerNotification.getTitle(),
                volunteerNotification.getContent(),
                volunteerNotification.getIsRead(),
                volunteerNotification.getType()
            );
        }
    }

    public static FindVolunteerNotificationsResponse from(List<VolunteerNotification> volunteerNotifications) {
        return new FindVolunteerNotificationsResponse(
            volunteerNotifications.stream()
                .map(FindVolunteerNotification::from)
                .toList()
        );
    }
}
