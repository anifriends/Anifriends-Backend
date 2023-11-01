package com.clova.anifriends.domain.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;

import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.jwt.response.UserToken;
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
        @DisplayName("성공")
        void authenticate() {
            //given
            UserToken userToken = jwtProvider.createToken(1L, UserRole.ROLE_VOLUNTEER);

            //when
            Authentication authentication = authenticationProvider.authenticate(
                userToken.accessToken());

            //then
            Object principal = authentication.getPrincipal();
            assertThat(principal.getClass()).isAssignableFrom(JwtAuthentication.class);
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
