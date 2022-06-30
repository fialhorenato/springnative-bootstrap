package com.renato.springbootstrap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/example")
public class ExampleController {

    @GetMapping("/authorized")
    public String authorizedMethod() {
        return "Authorized";
    }

    @GetMapping("/unauthorized")
    public String unauthorizedMethod() {
        return "Anyone can reach this method";
    }
}
