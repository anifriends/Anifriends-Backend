package com.clova.anifriends.global.exception;

import com.clova.anifriends.domain.auth.authorization.VolunteerOnly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ExceptionController {

    @GetMapping("/bad-request")
    public void badRequest() {
        throw new BadRequestException(ErrorCode.BAD_REQUEST, "bad-request") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/authentication")
    public void authentication() {
        throw new AuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED, "authentication") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/authorization")
    public void authorization() {
        throw new AuthorizationException(ErrorCode.UN_AUTHORIZATION, "authorization") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new NotFoundException(ErrorCode.NOT_FOUND, "notFound") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/conflict")
    public void conflict() {
        throw new ConflictException(ErrorCode.ALREADY_EXISTS, "conflict") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @VolunteerOnly
    @GetMapping("/access-denied")
    public void accessDenied() {

    }
}
