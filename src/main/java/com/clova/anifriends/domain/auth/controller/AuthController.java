package com.clova.anifriends.domain.auth.controller;

import com.clova.anifriends.domain.auth.controller.request.LoginRequest;
import com.clova.anifriends.domain.auth.service.AuthService;
import com.clova.anifriends.domain.auth.service.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/volunteer/login")
    public ResponseEntity<TokenResponse> volunteerLogin(@RequestBody LoginRequest request) {
        TokenResponse response = authService.volunteerLogin(request.email(), request.password());
        return ResponseEntity.ok(response);
    }
}
