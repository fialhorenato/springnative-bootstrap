package com.renato.springbootstrap.security.controller.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponse(
        @JsonProperty("access_token")
        String accessToken
) {}
