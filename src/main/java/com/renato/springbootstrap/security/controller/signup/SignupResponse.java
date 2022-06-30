package com.renato.springbootstrap.security.controller.signup;

import lombok.Builder;

@Builder(toBuilder = true)
public record SignupResponse(
        String username,
        String email
) {
}
