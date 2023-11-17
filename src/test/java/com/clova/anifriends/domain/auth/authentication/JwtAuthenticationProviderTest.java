package com.clova.anifriends.domain.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtAuthenticationProviderTest {

    JwtProvider jwtProvider = AuthFixture.jwtProvider();
    JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtProvider);

    @Nested
    @DisplayName("authenticate 메서드 실행 시")
    class AuthenticateTest {

        @Test
        @DisplayName("성공: principal인 JwtAuthentication이 정상적으로 만들어짐")
        void authenticateThenAuthenticationHasJwtAuthentication() {
            //given
            long userId = 1L;
            UserRole userRole = UserRole.ROLE_VOLUNTEER;
            TokenResponse tokenResponse = jwtProvider.createToken(userId, userRole);

            //when
            Authentication authentication = authenticationProvider.authenticate(
                tokenResponse.accessToken());

            //then
            Object principal = authentication.getPrincipal();
            assertThat(principal.getClass()).isAssignableFrom(JwtAuthentication.class);
            JwtAuthentication jwtAuthentication = (JwtAuthentication) principal;
            assertThat(jwtAuthentication.userId()).isEqualTo(userId);
            assertThat(jwtAuthentication.role()).isEqualTo(userRole);
            assertThat(jwtAuthentication.accessToken()).isEqualTo(tokenResponse.accessToken());
        }

        @Test
        @DisplayName("성공: 인증 객체인 UsernamePasswordAuthenticationToken이 정상적으로 만들어짐")
        void authenticateThenUsernamePasswordAuthenticationTokenIsPublish() {
            //given
            TokenResponse tokenResponse = jwtProvider.createToken(1L, UserRole.ROLE_VOLUNTEER);

            //when
            Authentication authentication = authenticationProvider.authenticate(
                tokenResponse.accessToken());

            //then
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = (UsernamePasswordAuthenticationToken) authentication;
            assertThat(usernamePasswordAuthenticationToken.getAuthorities())
                .containsExactlyElementsOf(
                    UserRole.ROLE_VOLUNTEER.getAuthorities().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList());
        }
    }
}
