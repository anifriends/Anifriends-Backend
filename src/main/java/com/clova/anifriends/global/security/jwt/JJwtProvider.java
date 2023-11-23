package com.clova.anifriends.global.security.jwt;

import com.clova.anifriends.domain.auth.dto.response.CustomClaims;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;
import com.clova.anifriends.domain.auth.exception.ExpiredAccessTokenException;
import com.clova.anifriends.domain.auth.exception.ExpiredRefreshTokenException;
import com.clova.anifriends.domain.auth.exception.InvalidJwtException;
import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JJwtProvider implements JwtProvider {

    private static final String ROLE = "role";

    private final String issuer;
    private final int expirySeconds;
    private final int refreshExpirySeconds;
    private final SecretKey secretKey;
    private final SecretKey refreshSecretKey;
    private final JwtParser accessTokenParser;
    private final JwtParser refreshTokenParser;


    public JJwtProvider(
        @Value("${jwt.issuer}") String issuer,
        @Value("${jwt.expiry-seconds}") int expirySeconds,
        @Value("${jwt.refresh-expiry-seconds}") int refreshExpirySeconds,
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.refresh-secret}") String refreshSecret) {
        this.issuer = issuer;
        this.expirySeconds = expirySeconds;
        this.refreshExpirySeconds = refreshExpirySeconds;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenParser = Jwts.parser()
            .verifyWith(secretKey)
            .build();
        this.refreshTokenParser = Jwts.parser()
            .verifyWith(refreshSecretKey)
            .build();
    }

    @Override
    public TokenResponse createToken(Long userId, UserRole userRole) {
        String accessToken = createAccessToken(userId, userRole);
        String refreshToken = createRefreshToken(userId, userRole);
        return TokenResponse.of(userId, userRole, accessToken, refreshToken);
    }

    private String createAccessToken(Long userId, UserRole userRole) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expirySeconds * 1000L);
        return Jwts.builder()
            .issuer(issuer)
            .issuedAt(now)
            .subject(userId.toString())
            .expiration(expiresAt)
            .claim(ROLE, userRole.getValue())
            .signWith(secretKey)
            .compact();
    }

    private String createRefreshToken(Long userId, UserRole userRole) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + refreshExpirySeconds * 1000L);
        return Jwts.builder()
            .issuer(issuer)
            .issuedAt(now)
            .subject(userId.toString())
            .expiration(expiresAt)
            .claim(ROLE, userRole.getValue())
            .signWith(refreshSecretKey)
            .compact();
    }

    @Override
    public CustomClaims parseAccessToken(String token) {
        try {
            Claims claims = accessTokenParser.parseSignedClaims(token).getPayload();
            Long userId = Long.valueOf(claims.getSubject());
            UserRole userRole = UserRole.valueOf(claims.get(ROLE, String.class));
            List<String> authorities = userRole.getAuthorities();
            return CustomClaims.of(userId, userRole, authorities);
        } catch (ExpiredJwtException ex) {
            log.info("[Ex] {} 만료된 JWT입니다.", ex.getClass().getSimpleName());
            throw new ExpiredAccessTokenException("만료된 액세스 토큰입니다.");
        } catch (JwtException ex) {
            log.info("[Ex] {} 잘못된 JWT입니다.", ex.getClass().getSimpleName());
        }
        throw new InvalidJwtException("유효하지 않은 JWT입니다.");
    }

    @Override
    public TokenResponse refreshAccessToken(String refreshToken) {
        try {
            Claims claims = refreshTokenParser.parseSignedClaims(refreshToken).getPayload();
            Long userId = Long.valueOf(claims.getSubject());
            UserRole userRole = UserRole.valueOf(claims.get(ROLE, String.class));
            return createToken(userId, userRole);
        } catch (ExpiredJwtException ex) {
            log.info("[EX] {} 만료된 리프레시 토큰입니다.", ex.getClass().getSimpleName());
            throw new ExpiredRefreshTokenException("만료된 리프레시 토큰입니다.");
        } catch (JwtException ex) {
            log.info("[EX] {} 잘못된 리프레시 토큰입니다.", ex.getClass().getSimpleName());
        }
        throw new InvalidJwtException("유효하지 않은 리프레시 토큰입니다.");
    }
}
