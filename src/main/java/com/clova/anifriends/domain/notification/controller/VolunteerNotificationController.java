package com.clova.anifriends.domain.notification.controller;


import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.notification.dto.FindVolunteerNotificationsResponse;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VolunteerNotificationController {

    private final VolunteerNotificationService volunteerNotificationService;

    @GetMapping("/volunteers/notifications")
    public ResponseEntity<FindVolunteerNotificationsResponse> findShelterNotifications(
        @LoginUser Long volunteerId
    ) {
        return ResponseEntity.ok(
            volunteerNotificationService.findVolunteerNotifications(volunteerId));
    }
}
