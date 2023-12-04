package com.clova.anifriends.domain.notification.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.notification.dto.response.FindShelterHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import com.clova.anifriends.domain.auth.authorization.ShelterOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShelterNotificationController {

    private final ShelterNotificationService shelterNotificationService;

    @ShelterOnly
    @GetMapping("/shelters/notifications")
    public ResponseEntity<FindShelterNotificationsResponse> findShelterNotifications(
        @LoginUser Long shelterId
    ) {
        return ResponseEntity.ok(shelterNotificationService.findShelterNotifications(shelterId));
    }

    @ShelterOnly
    @GetMapping("/shelters/notifications/read")
    public ResponseEntity<FindShelterHasNewNotificationResponse> findShelterHasNewNotification(
        @LoginUser Long shelterId
    ) {
        return ResponseEntity.ok(
            shelterNotificationService.findShelterHasNewNotification(shelterId));
    }

    @ShelterOnly
    @PatchMapping("/shelters/notification/read")
    public ResponseEntity<Void> updateNotificationRead(
        @LoginUser Long shelterId
    ) {
        shelterNotificationService.updateNotificationRead(shelterId);
        return ResponseEntity.noContent().build();
    }
}
