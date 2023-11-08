package com.clova.anifriends.domain.auth.resolver;

import com.clova.anifriends.domain.auth.LoginUser;
import org.springframework.http.ResponseEntity;
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
}
