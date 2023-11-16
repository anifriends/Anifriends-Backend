package com.clova.anifriends.domain.notification.controller;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.notification.dto.response.FindShelterNotificationsResponse;
import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ShelterNotificationController {

    private final ShelterNotificationService shelterNotificationService;

    @GetMapping("/shelters/notifications")
    public ResponseEntity<FindShelterNotificationsResponse> findShelterNotifications(
        @LoginUser Long shelterId
    ) {
        return ResponseEntity.ok(shelterNotificationService.findShelterNotifications(shelterId));
    }
}
