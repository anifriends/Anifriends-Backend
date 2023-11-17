package com.clova.anifriends.domain.auth.resolver;

import com.clova.anifriends.domain.auth.LoginUser;
import com.clova.anifriends.domain.auth.authentication.JwtAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class LoginUserTestController {

    @GetMapping("/login-user")
    public ResponseEntity<Long> loginUser(@LoginUser Long memberId) {
        return ResponseEntity.ok(memberId);
    }

    @GetMapping("/login-user/invalid")
    public ResponseEntity<String> invalidLoginUser(@LoginUser String memberId) {
        return ResponseEntity.ok(memberId);
    }

    @GetMapping("/authentication-principal")
    public ResponseEntity<JwtAuthentication> jwtAuthentication(
        @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        return ResponseEntity.ok(jwtAuthentication);
    }
}
