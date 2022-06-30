package com.renato.springbootstrap.security.service;

import com.renato.springbootstrap.security.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtTokenProvider.getUserNameFromJwtToken(authToken);
        return Mono.just(jwtTokenProvider.validateJwtToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> {
                    List<String> roles = jwtTokenProvider.getRolesFromJwtToken(authToken);
                    return new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            jwtTokenProvider.getAuthoritiesFromJwtToken(authToken)
                    );
                });
    }
}