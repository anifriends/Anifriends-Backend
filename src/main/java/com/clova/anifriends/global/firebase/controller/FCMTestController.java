package com.clova.anifriends.global.firebase.controller;

import com.clova.anifriends.domain.notification.wrapper.NotificationType;
import com.clova.anifriends.global.firebase.dto.FCMTestRequest;
import com.clova.anifriends.global.firebase.service.FCMService;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/fcm")
public class FCMTestController {

    private final FCMService fcmService;

    @PostMapping
    public ResponseEntity<Void> pushNotification(
        @Valid @RequestBody FCMTestRequest fcmTestRequest
    ) {
        try {
            fcmService.sendMessage(fcmTestRequest.deviceToken(),
                "test 제목",
                "test 내용",
                NotificationType.NEW_APPLICANT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.noContent().build();
    }
}
