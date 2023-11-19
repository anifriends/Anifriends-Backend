package com.clova.anifriends.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.clova.anifriends.domain.auth.RefreshToken;
import com.clova.anifriends.domain.auth.dto.response.TokenResponse;
import com.clova.anifriends.domain.auth.exception.AuthAuthenticationException;
import com.clova.anifriends.domain.auth.exception.AuthNotFoundException;
import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.jwt.UserRole;
import com.clova.anifriends.domain.auth.repository.RefreshTokenRepository;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import com.clova.anifriends.domain.auth.support.MockPasswordEncoder;
import com.clova.anifriends.domain.common.CustomPasswordEncoder;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.repository.ShelterRepository;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    VolunteerRepository volunteerRepository;

    @Mock
    ShelterRepository shelterRepository;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Spy
    CustomPasswordEncoder passwordEncoder = new MockPasswordEncoder();

    @Spy
    JwtProvider jwtProvider = AuthFixture.jwtProvider();

    @Nested
    @DisplayName("login 메서드 실행 시")
    class LoginTest {

        String email = "email@email.com";
        String password = "password123!";
        String deviceToken = "token";
        Volunteer volunteer = new Volunteer(email, password,
            LocalDate.now().toString(), "010-1234-1234", "MALE", "name", passwordEncoder);

        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
        }

        @Test
        @DisplayName("성공")
        void volunteerLogin() {
            //given
            given(volunteerRepository.findByEmail(any())).willReturn(
                Optional.ofNullable(volunteer));

            //when
            TokenResponse response = authService.volunteerLogin(email, password, deviceToken);

            //then
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.refreshToken()).isNotBlank();
            assertThat(volunteer.getDeviceToken()).isEqualTo(deviceToken);
        }

        @Test
        @DisplayName("성공: 리프레시 토큰 저장됨")
        void saveRefreshToken() {
            //given
            given(volunteerRepository.findByEmail(any())).willReturn(
                Optional.ofNullable(volunteer));

            //when
            TokenResponse response = authService.volunteerLogin(email, password, deviceToken);

            //then
            then(refreshTokenRepository).should().save(any());
        }

        @Test
        @DisplayName("성공: deviceToken이 null인 경우")
        void loginWhenDeviceTokenIsNull() {
            // given
            deviceToken = null;
            given(volunteerRepository.findByEmail(any())).willReturn(
                Optional.ofNullable(volunteer));

            // when
            TokenResponse response = authService.volunteerLogin(email, password, deviceToken);

            // then
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.refreshToken()).isNotBlank();
        }

        @Test
        @DisplayName("예외(AuthAuthenticationException): 비밀번호가 다름")
        void exceptionWhenNotEqualsPassword() {
            //given
            String notEqualsPassword = password + "a";

            given(volunteerRepository.findByEmail(any())).willReturn(
                Optional.ofNullable(volunteer));

            //when
            Exception exception = catchException(
                () -> authService.volunteerLogin(email, notEqualsPassword, deviceToken));

            //then
            assertThat(exception).isInstanceOf(AuthAuthenticationException.class);
        }

        @Test
        @DisplayName("예외(AuthAuthenticationException): 존재하지 않는 봉사자")
        void exceptionWhenVolunteerNotFound() {
            //given
            given(volunteerRepository.findByEmail(any())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> authService.volunteerLogin(email, password, deviceToken));

            //then
            assertThat(exception).isInstanceOf(AuthAuthenticationException.class);
        }
    }

    @Nested
    @DisplayName("shelterLogin 메서드 실행 시")
    class ShelterLoginTest {

        String email = "email@email.com";
        String password = "password123!";
        String deviceToken = "token";
        Shelter shelter = new Shelter(email, password, "address",
            "addressDetail", "name", "02-1234-5678", "02-1234-5678", false, passwordEncoder);

        @BeforeEach
        void setUp() {
            ReflectionTestUtils.setField(shelter, "shelterId", 1L);
        }

        @Test
        @DisplayName("성공")
        void shelterLogin() {
            //given
            given(shelterRepository.findByEmail(any())).willReturn(Optional.ofNullable(shelter));

            //when
            TokenResponse response = authService.shelterLogin(email, password, deviceToken);

            //then
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.refreshToken()).isNotBlank();
            assertThat(shelter.getDeviceToken()).isEqualTo(deviceToken);
        }

        @Test
        @DisplayName("성공: 리프레시 토큰 저장됨")
        void saveRefreshToken() {
            //given
            given(shelterRepository.findByEmail(any())).willReturn(
                Optional.ofNullable(shelter));

            //when
            TokenResponse response = authService.shelterLogin(email, password, deviceToken);

            //then
            then(refreshTokenRepository).should().save(any());
        }

        @Test
        @DisplayName("성공: deviceToken이 null인 경우")
        void loginWhenDeviceTokenIsNull() {
            // given
            deviceToken = null;
            given(shelterRepository.findByEmail(any())).willReturn(Optional.ofNullable(shelter));

            // when
            TokenResponse response = authService.shelterLogin(email, password, deviceToken);

            // then
            assertThat(response.accessToken()).isNotBlank();
            assertThat(response.refreshToken()).isNotBlank();
        }

        @Test
        @DisplayName("예외(AuthAuthenticationException): 비밀번호가 다름")
        void exceptionWhenNotEqualsPassword() {
            //given
            String notEqualsPassword = password + "a";

            given(shelterRepository.findByEmail(any())).willReturn(
                Optional.ofNullable(shelter));

            //when
            Exception exception = catchException(
                () -> authService.shelterLogin(email, notEqualsPassword, deviceToken));

            //then
            assertThat(exception).isInstanceOf(AuthAuthenticationException.class);
        }

        @Test
        @DisplayName("예외(AuthAuthenticationException): 존재하지 않는 보호소")
        void exceptionWhenShelterNotFound() {
            //given
            given(shelterRepository.findByEmail(any())).willReturn(Optional.empty());

            //when
            Exception exception = catchException(() -> authService.shelterLogin(email, password, deviceToken));

            //then
            assertThat(exception).isInstanceOf(AuthAuthenticationException.class);
        }
    }

    @Nested
    @DisplayName("refreshAccessToken 메서드 실행 시")
    class RefreshAccessTokenTest {

        @Test
        @DisplayName("성공")
        void refreshAccessToken() {
            //given
            Long userId = AuthFixture.USER_ID;
            UserRole userRole = AuthFixture.USER_ROLE;
            TokenResponse tokenResponse = AuthFixture.userToken();
            String refreshTokenValue = tokenResponse.refreshToken();
            RefreshToken refreshTokenEntity = new RefreshToken(refreshTokenValue, 1L,
                UserRole.ROLE_VOLUNTEER);

            given(refreshTokenRepository.findByTokenValue(anyString()))
                .willReturn(Optional.of(refreshTokenEntity));

            //when
            //then
            Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
                TokenResponse resultTokenResponse = authService.refreshAccessToken(
                    refreshTokenValue);

                assertThat(resultTokenResponse.refreshToken()).isNotEqualTo(refreshTokenValue);
                assertThat(resultTokenResponse.accessToken()).isNotBlank();
                assertThat(resultTokenResponse.userId()).isEqualTo(userId);
                assertThat(resultTokenResponse.role()).isEqualTo(userRole);
            });
        }

        @Test
        @DisplayName("예외(AuthNotFoundException): 잘못된 리프레시 토큰")
        void exceptionWhenRefreshTokenExpired() {
            //given
            TokenResponse tokenResponse = AuthFixture.userToken();
            String refreshTokenValue = tokenResponse.refreshToken();

            given(refreshTokenRepository.findByTokenValue(anyString()))
                .willReturn(Optional.empty());

            //when
            Exception exception = catchException(
                () -> authService.refreshAccessToken(refreshTokenValue));

            //then
            assertThat(exception).isInstanceOf(AuthNotFoundException.class);
        }
    }
}
