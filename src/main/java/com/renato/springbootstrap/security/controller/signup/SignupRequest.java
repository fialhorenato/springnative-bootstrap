package com.renato.springbootstrap.security.controller.signup;

public record SignupRequest(
        String username,
        String password,
        String email
) {
}
