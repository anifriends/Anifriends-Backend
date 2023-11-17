package com.clova.anifriends.global.web.filter;

import com.clova.anifriends.domain.auth.authentication.JwtAuthenticationProvider;
import com.clova.anifriends.domain.auth.exception.InvalidJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private static final String HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtAuthenticationProvider authenticationProvider;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String bearerAccessToken = request.getHeader(HEADER);
        if (Objects.nonNull(bearerAccessToken)) {
            String accessToken = removeBearer(bearerAccessToken);
            Authentication authentication = authenticationProvider.authenticate(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }

    private String removeBearer(String bearerAccessToken) {
        checkTokenContainsBearer(bearerAccessToken);
        return bearerAccessToken.replace(BEARER, "");
    }

    private static void checkTokenContainsBearer(String bearerAccessToken) {
        if(!bearerAccessToken.contains(BEARER)) {
            throw new InvalidJwtException("올바르지 않는 액세스 토큰 형식입니다.");
        }
    }
}
