package com.renato.springbootstrap.security.controller;

import com.renato.springbootstrap.security.controller.login.LoginRequest;
import com.renato.springbootstrap.security.controller.login.LoginResponse;
import com.renato.springbootstrap.security.controller.signup.SignupRequest;
import com.renato.springbootstrap.security.controller.signup.SignupResponse;
import com.renato.springbootstrap.security.repository.entity.UserEntity;
import com.renato.springbootstrap.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var token =  securityService.authenticate(
                loginRequest.username(),
                loginRequest.password()
        );

        return token.map(this::toLoginResponse);
    }

    private LoginResponse toLoginResponse(String accessToken) {
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    @PostMapping("/signup")
    public Mono<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        return securityService.createUser(
                signupRequest.username(),
                signupRequest.password(),
                signupRequest.email()
        ).map(this::toSignupResponse);
    }

    @GetMapping("/env")
    public Mono<Map<String, String>> getEnvVars() {
        return Mono.just(System.getenv());
    }

    private SignupResponse toSignupResponse(UserEntity userEntity) {
        return SignupResponse.builder()
                .username(userEntity.username())
                .email(userEntity.email())
                .build();
    }
}
