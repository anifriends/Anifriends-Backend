package com.clova.anifriends.domain.auth.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

import com.clova.anifriends.domain.auth.authentication.JwtAuthenticationProvider;
import com.clova.anifriends.domain.auth.exception.InvalidJwtException;
import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import com.clova.anifriends.global.web.filter.JwtAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    JwtProvider jwtProvider = AuthFixture.jwtProvider();
    JwtAuthenticationProvider jwtAuthenticationProvider = new JwtAuthenticationProvider(
        jwtProvider);
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
        jwtAuthenticationProvider);

    MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    MockHttpServletResponse mockResponse = new MockHttpServletResponse();

    @Mock
    FilterChain filterChain;

    @Nested
    @DisplayName("doFilter 메서드 실행 시")
    class DoFilterTest {

        @Test
        @DisplayName("성공: 다음 필터를 호출한다.")
        void doFilter() throws ServletException, IOException {
            //given
            //when
            jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, filterChain);

            //then
            then(filterChain).should().doFilter(any(), any());
        }

        @Test
        @DisplayName("성공: 액세스 토큰이 요청에 포함된 경우")
        void doFilterWhenContainsToken() throws ServletException, IOException {
            //given
            String accessToken = AuthFixture.shelterAccessToken();
            mockRequest.addHeader("Authorization", accessToken);

            //when
            jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, filterChain);

            //then
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication).isNotNull();
        }

        @Test
        @DisplayName("성공: 액세스 토큰이 요청에 포함되지 않은 경우")
        void doFilterWhenNotContainsToken() throws ServletException, IOException {
            //given
            //when
            jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, filterChain);

            //then
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication).isNull();
        }

        @Test
        @DisplayName("예외: 액세스 토큰이 Bearer 형식이 아닌 경우")
        void exceptionWhenTokenIsNotBearer() {
            //given
            String bearerAccessToken = AuthFixture.volunteerAccessToken();
            String accessToken = bearerAccessToken.replace("Bearer ", "");
            mockRequest.addHeader("Authorization", accessToken);

            //when
            Exception exception = catchException(
                () -> jwtAuthenticationFilter.doFilter(mockRequest, mockResponse, filterChain));

            //then
            assertThat(exception).isInstanceOf(InvalidJwtException.class);
        }
    }
}
