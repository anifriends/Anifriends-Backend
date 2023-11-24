package com.clova.anifriends.global.web.filter;

import com.clova.anifriends.domain.auth.exception.ExpiredAccessTokenException;
import com.clova.anifriends.domain.auth.exception.InvalidJwtException;
import com.clova.anifriends.global.exception.ErrorCode;
import com.clova.anifriends.global.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredAccessTokenException e) {
            log.debug("[Authentication] JWT 인증 예외 핸들링. 만료된 JWT");
            ErrorResponse errorResponse
                = new ErrorResponse(ErrorCode.ACCESS_TOKEN_EXPIRED.getValue(), "액세스 토큰이 만료되었습니다.");
            setResponse(response, errorResponse);
        } catch (InvalidJwtException e) {
            log.debug("[Authentication] JWT 인증 예외 핸들링. 잘못된 JWT");
            ErrorResponse errorResponse
                = new ErrorResponse(ErrorCode.UN_AUTHENTICATION.getValue(), "인증되지 않은 사용자의 요청입니다.");
            setResponse(response, errorResponse);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorResponse errorResponse)
        throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
