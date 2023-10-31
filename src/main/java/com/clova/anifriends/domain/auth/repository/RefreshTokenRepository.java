package com.clova.anifriends.domain.auth.repository;

import com.clova.anifriends.domain.auth.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByValue(String refreshToken);
}
