package com.renato.springbootstrap.security.filter;

import com.renato.springbootstrap.security.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    public static final String HEADER_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    private final JwtTokenProvider tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var request = exchange.getRequest();

        var authorizationHeader = request
                .getHeaders()
                .getOrEmpty(HEADER)
                .stream().findFirst();

        var token = authorizationHeader
                .stream()
                .filter(header -> header.startsWith(HEADER_PREFIX))
                .map(this::getToken)
                .findFirst();

        if (token.isEmpty()) {
            return chain.filter(exchange);
        }

        if(tokenProvider.validateJwtToken(token.get())) {
            var userDetails = tokenProvider.toUserDetails(token.get());
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        return chain.filter(exchange);
    }

    private String getToken(String header) {
        return header.substring(7);
    }
}
