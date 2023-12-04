package com.clova.anifriends.domain.notification.dto.response;

import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import java.util.List;

public record FindShelterNotificationsResponse(
    List<FindShelterNotification> notifications
) {

    private record FindShelterNotification(
        Long notificationId,
        String notificationTitle,
        String notificationContent,
        Boolean notificationIsRead,
        NotificationType notificationType
    ) {

        private static FindShelterNotification from(ShelterNotification shelterNotification) {
            return new FindShelterNotification(
                shelterNotification.getShelterNotificationId(),
                shelterNotification.getTitle(),
                shelterNotification.getContent(),
                shelterNotification.getIsRead(),
                shelterNotification.getType()
            );
        }
    }

    public static FindShelterNotificationsResponse from(List<ShelterNotification> shelterNotifications) {
        return new FindShelterNotificationsResponse(
            shelterNotifications.stream()
                .map(FindShelterNotification::from)
                .toList()
        );
    }
}
