package com.clova.anifriends.global.web.argumentresolver;

import static com.clova.anifriends.global.exception.ErrorCode.UN_AUTHENTICATION;

import com.clova.anifriends.domain.auth.authentication.JwtAuthentication;
import com.clova.anifriends.domain.auth.exception.AuthAuthenticationException;
import com.clova.anifriends.domain.auth.LoginUser;
import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean hasLongParameterType = parameter.getParameterType().isAssignableFrom(Long.class);
        return hasParameterAnnotation && hasLongParameterType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        checkAuthenticated(authentication);
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();
        return jwtAuthentication.userId();
    }

    private void checkAuthenticated(Authentication authentication) {
        if(Objects.isNull(authentication)) {
            throw new AuthAuthenticationException(UN_AUTHENTICATION, "인증되지 않은 요청입니다.");
        }
    }
}
