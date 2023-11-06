package com.clova.anifriends.domain.auth.controller;

import com.clova.anifriends.domain.auth.controller.request.LoginRequest;
import com.clova.anifriends.domain.auth.jwt.response.TokenResponse;
import com.clova.anifriends.domain.auth.service.AuthService;
import jakarta.validation.Valid;
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

    @PostMapping("/volunteers/login")
    public ResponseEntity<TokenResponse> volunteerLogin(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = authService.volunteerLogin(request.email(), request.password());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/shelters/login")
    public ResponseEntity<TokenResponse> shelterLogin(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = authService.shelterLogin(request.email(), request.password());
        return ResponseEntity.ok(response);
    }
}
