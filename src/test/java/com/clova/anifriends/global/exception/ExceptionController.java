package com.clova.anifriends.global.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ExceptionController {

    @GetMapping("/bad-request")
    public void badRequest() {
        throw new BadRequestException("bad-request") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/authentication")
    public void authentication() {
        throw new AuthenticationException("authentication") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/authorization")
    public void authorization() {
        throw new AuthorizationException("authorization") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new NotFoundException("notFound") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }

    @GetMapping("/conflict")
    public void conflict() {
        throw new ConflictException("conflict") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        };
    }
}
