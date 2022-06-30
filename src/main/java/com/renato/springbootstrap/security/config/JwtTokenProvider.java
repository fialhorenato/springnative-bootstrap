package com.renato.springbootstrap.security.config;

import com.renato.springbootstrap.security.service.UserDetails;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.nimbusds.jose.JWSAlgorithm.HS256;

@Component
@Slf4j
public class JwtTokenProvider {

    public final static String ROLE_PREFIX = "ROLE_";
    private final static String SUBJECT_CLAIM = "sub";
    private final static String EMAIL_CLAIM = "email";
    private final static String ROLES_CLAIM = "roles";
    private final static String PASSWORD_CLAIM = "password";

    @Value("${jwt.jwt-secret}")
    private String jwtSecret;

    @Value("${jwt.jwt-expiration-ms}")
    private Long jwtExpirationMs;

    @SneakyThrows
    public String generateJwtToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
        if (userDetails instanceof UserDetails details) {
            var payload = new Payload(getClaims(details).toJSONObject());
            var header = new JWSHeader(HS256);
            var signer = new MACSigner(jwtSecret);
            var jwsObject = new JWSObject(header, payload);

            jwsObject.sign(signer);
            return jwsObject.serialize();
        }

        return "UNAUTHORIZED";
    }

    public UserDetails toUserDetails(String token) {
        var username = getUserNameFromJwtToken(token);
        var email = getEmailFromJwtToken(token);
        var password = getPasswordFromJwtToken(token);
        var authorities = getAuthoritiesFromJwtToken(token);
        var roles = getRolesFromJwtToken(token);

        return new UserDetails(username, password, authorities, roles, email);
    }

    @SneakyThrows
    public Boolean validateJwtToken(String token) {
        var verifier = new MACVerifier(jwtSecret);
        return JWSObject.parse(token).verify(verifier);
    }

    public JWTClaimsSet getClaims(UserDetails userDetails) {
        return new JWTClaimsSet.Builder()
                .claim(EMAIL_CLAIM, userDetails.getEmail())
                .claim(ROLES_CLAIM, userDetails.getRoles())
                .claim(PASSWORD_CLAIM, userDetails.getPassword())
                .expirationTime(new Date(new Date().getTime() + jwtExpirationMs))
                .issueTime(new Date())
                .subject(userDetails.getUsername())
                .build();
    }

    @SneakyThrows
    public String getUserNameFromJwtToken(String token) {
        return JWSObject.parse(token)
                .getPayload()
                .toJSONObject()
                .get(SUBJECT_CLAIM)
                .toString();
    }

    @SneakyThrows
    public String getPasswordFromJwtToken(String token) {
        return JWSObject.parse(token)
                .getPayload()
                .toJSONObject()
                .get(PASSWORD_CLAIM)
                .toString();
    }

    @SneakyThrows
    public String getEmailFromJwtToken(String token) {
        return JWSObject.parse(token)
                .getPayload()
                .toJSONObject()
                .get(EMAIL_CLAIM)
                .toString();
    }

    @SneakyThrows
    public List<String> getRolesFromJwtToken(String token) {
        var roles = JWSObject.parse(token)
                .getPayload()
                .toJSONObject()
                .get(ROLES_CLAIM);

        if (roles instanceof List rolesList) {
            return (List<String>) rolesList
                    .stream()
                    .filter(String.class::isInstance)
                    .toList();
        }

        return Collections.emptyList();
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        return getRolesFromJwtToken(token)
                .stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .toList();
    }
}
