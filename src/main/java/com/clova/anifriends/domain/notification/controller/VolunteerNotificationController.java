package com.clova.anifriends.domain.notification.controller;


import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerHasNewNotificationResponse;
import com.clova.anifriends.domain.notification.dto.response.FindVolunteerNotificationsResponse;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import com.clova.anifriends.domain.auth.authorization.VolunteerOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class VolunteerNotificationController {

    private final VolunteerNotificationService volunteerNotificationService;

    @VolunteerOnly
    @GetMapping("/volunteers/notifications")
    public ResponseEntity<FindVolunteerNotificationsResponse> findShelterNotifications(
        @LoginUser Long volunteerId
    ) {
        return ResponseEntity.ok(
            volunteerNotificationService.findVolunteerNotifications(volunteerId));
    }

    @VolunteerOnly
    @GetMapping("/volunteers/notifications/read")
    public ResponseEntity<FindVolunteerHasNewNotificationResponse> findVolunteerHasNewNotification(
        @LoginUser Long volunteerId
    ) {
        return ResponseEntity.ok(
            volunteerNotificationService.findVolunteerHasNewNotification(volunteerId));
    }

    @VolunteerOnly
    @PatchMapping("/volunteers/notifications/read")
    public ResponseEntity<Void> updateNotificationRead(
        @LoginUser Long volunteerId
    ) {
        volunteerNotificationService.updateNotificationRead(volunteerId);
        return ResponseEntity.noContent().build();
    }
}
