package com.clova.anifriends.domain.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.await;

import com.clova.anifriends.domain.auth.exception.ExpiredAccessTokenException;
import com.clova.anifriends.domain.auth.exception.ExpiredRefreshTokenException;
import com.clova.anifriends.domain.auth.exception.InvalidJwtException;
import com.clova.anifriends.domain.auth.jwt.response.CustomClaims;
import com.clova.anifriends.domain.auth.jwt.response.UserToken;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JJwtProviderTest {

    String issuer = "issuer";
    int expirySeconds = 1000;
    int refreshExpirySeconds = 10000;
    String testSecret = "}:ASV~lS,%!I:ba^GBR<Q@cJN~!,Y0=zx7Rqwum+remZ>ayhI3$4dX$jx~@9[1F";
    String testRefreshSecret = "~GWW.|?:\"#Rqmm^-nk#>#4Ngc}]3xz!hOQCXNF:8z-Mdn\"U!Vt</+/8;ATR*lc{";
    JJwtProvider jJwtProvider = AuthFixture.jJwtProvider();
    UserToken userToken = AuthFixture.userToken();
    Long userId = 1L;
    UserRole roleVolunteer = UserRole.ROLE_VOLUNTEER;

    @Nested
    @DisplayName("createToken 메서드 실행 시")
    class CreateTokenTest {

        @Test
        @DisplayName("성공")
        void createToken() {
            //given
            Long userId = 1L;
            UserRole volunteerRole = UserRole.ROLE_VOLUNTEER;

            //when
            UserToken UserToken = jJwtProvider.createToken(userId, volunteerRole);

            //then
            assertThat(UserToken.accessToken()).isNotBlank();
            assertThat(UserToken.refreshToken()).isNotBlank();
            assertThat(UserToken.accessToken()).isNotEqualTo(UserToken.refreshToken());
        }
    }

    @Nested
    @DisplayName("parseAccessToken 메서드 실행 시")
    class ParseAccessTokenTest {

        @Test
        @DisplayName("성공")
        void parseAccessToken() {
            //given
            UserToken userToken = jJwtProvider.createToken(userId, roleVolunteer);

            //when
            CustomClaims claims = jJwtProvider.parseAccessToken(userToken.accessToken());

            //then
            Long findUserId = claims.memberId();
            List<String> authorities = claims.authorities();
            assertThat(findUserId).isEqualTo(userId);
            assertThat(authorities).containsExactlyElementsOf(roleVolunteer.getAuthorities());
        }

        @Test
        @DisplayName("예외(InvalidJwtException): 잘못된 토큰")
        void exceptionWhenInvalidJwt() {
            //given
            String invalidSecret = testSecret + "invalid";
            JJwtProvider invalidJJwtProvider = new JJwtProvider(issuer, expirySeconds,
                refreshExpirySeconds, invalidSecret, testRefreshSecret);
            UserToken UserToken = invalidJJwtProvider.createToken(userId, roleVolunteer);
            String invalidAccessToken = UserToken.accessToken();

            //when
            //then
            assertThatThrownBy(
                () -> jJwtProvider.parseAccessToken(invalidAccessToken))
                .isInstanceOf(InvalidJwtException.class);
        }

        @Test
        @DisplayName("예외(ExpiredAccessTokenException): 만료된 토큰")
        void exceptionWhenExpiredJwt() {
            //given
            int expirySeconds = -1;
            JJwtProvider expiredJJwtProvider = new JJwtProvider(issuer, expirySeconds,
                refreshExpirySeconds, testSecret, testRefreshSecret);
            UserToken userToken = expiredJJwtProvider.createToken(userId, roleVolunteer);
            String expiredAccessToken = userToken.accessToken();

            //when
            //then
            assertThatThrownBy(() -> jJwtProvider.parseAccessToken(expiredAccessToken))
                .isInstanceOf(ExpiredAccessTokenException.class);
        }

        @Test
        @DisplayName("예외(InvalidJwtException): 리프레시 토큰 사용 불가")
        void exceptionWhenUsingRefreshToken() {
            //given
            String refreshToken = userToken.refreshToken();

            //when
            //then
            assertThatThrownBy(() -> jJwtProvider.parseAccessToken(refreshToken))
                .isInstanceOf(InvalidJwtException.class);
        }
    }

    @Nested
    @DisplayName("RefreshAccessToken 메서드 실행 시")
    class RefreshAccessTokenTest {

        @Test
        @DisplayName("성공")
        void refreshAccessToken() {
            //given
            String refreshToken = userToken.refreshToken();

            //when
            //then
            await().atLeast(10, TimeUnit.MILLISECONDS).untilAsserted(() ->
            {
                UserToken newUserToken = jJwtProvider.refreshAccessToken(refreshToken);
                assertThat(newUserToken.accessToken()).isNotEqualTo(refreshToken);
            });
        }

        @Test
        @DisplayName("예외(InvalidJwtException): 액세스 토큰 사용 불가")
        void exceptionWhenUsingAccessToken() {
            //given
            String accessToken = userToken.accessToken();

            //when
            //then
            assertThatThrownBy(() -> jJwtProvider.refreshAccessToken(accessToken))
                .isInstanceOf(InvalidJwtException.class);
        }

        @Test
        @DisplayName("예외(ExpiredRefreshTokenException): 만료된 리프레시 토큰")
        void exceptionWhenRefreshTokenIsExpired() {
            //given
            int refreshExpirySeconds = -1;
            JJwtProvider expiredJJwtProvider = new JJwtProvider(issuer, expirySeconds,
                refreshExpirySeconds, testSecret, testRefreshSecret);
            UserToken userToken = expiredJJwtProvider.createToken(userId, roleVolunteer);
            String expiredRefreshToken = userToken.refreshToken();

            //when
            //then
            assertThatThrownBy(() -> jJwtProvider.refreshAccessToken(expiredRefreshToken))
                .isInstanceOf(ExpiredRefreshTokenException.class);
        }
    }
}
