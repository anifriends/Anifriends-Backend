package com.clova.anifriends.domain.auth;

import com.clova.anifriends.domain.auth.jwt.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    private String tokenValue;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public RefreshToken(String value, Long userId, UserRole userRole) {
        this.tokenValue = value;
        this.userId = userId;
        this.userRole = userRole;
    }
}
