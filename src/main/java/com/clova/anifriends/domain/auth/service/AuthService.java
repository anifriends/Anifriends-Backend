package com.clova.anifriends.domain.auth.service;

import static com.clova.anifriends.global.exception.ErrorCode.INVALID_AUTH_INFO;

import com.clova.anifriends.domain.auth.RefreshToken;
import com.clova.anifriends.domain.auth.exception.AuthAuthenticationException;
import com.clova.anifriends.domain.auth.exception.AuthNotFoundException;
import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.jwt.response.TokenResponse;
import com.clova.anifriends.domain.auth.repository.RefreshTokenRepository;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.shelter.wrapper.ShelterEmail;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.domain.volunteer.wrapper.VolunteerEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String NOT_EQUALS_AUTH_INFO = "아이디/비밀번호가 일치하지 않습니다.";

    private final VolunteerRepository volunteerRepository;
    private final ShelterRepository shelterRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse volunteerLogin(String email, String password) {
        Volunteer volunteer = volunteerRepository.findByEmail(new VolunteerEmail(email))
            .orElseThrow(
                () -> new AuthAuthenticationException(INVALID_AUTH_INFO, NOT_EQUALS_AUTH_INFO));
        validatePassword(password, volunteer.getPassword());
        return createToken(volunteer.getVolunteerId(), UserRole.ROLE_VOLUNTEER);
    }

    @Transactional
    public TokenResponse shelterLogin(String email, String password) {
        Shelter shelter = shelterRepository.findByEmail(new ShelterEmail(email))
            .orElseThrow(
                () -> new AuthAuthenticationException(INVALID_AUTH_INFO, NOT_EQUALS_AUTH_INFO));
        validatePassword(password, shelter.getPassword());
        return createToken(shelter.getShelterId(), UserRole.ROLE_SHELTER);
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new AuthAuthenticationException(INVALID_AUTH_INFO, NOT_EQUALS_AUTH_INFO);
        }
    }

    public TokenResponse createToken(Long userId, UserRole userRole) {
        TokenResponse tokenResponse = jwtProvider.createToken(userId, userRole);
        refreshTokenRepository.save(
            new RefreshToken(tokenResponse.refreshToken(), userId, userRole));
        return tokenResponse;
    }

    @Transactional
    public TokenResponse refreshAccessToken(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByTokenValue(refreshToken)
            .orElseThrow(() -> new AuthNotFoundException("토큰 정보가 유효하지 않습니다."));
        TokenResponse tokenResponse = jwtProvider.refreshAccessToken(refreshToken);
        refreshTokenRepository.delete(findRefreshToken);
        refreshTokenRepository.save(new RefreshToken(tokenResponse.refreshToken(),
            tokenResponse.userId(), tokenResponse.role()));
        return tokenResponse;
    }
}
