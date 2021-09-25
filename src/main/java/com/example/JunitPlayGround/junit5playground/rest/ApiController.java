package com.example.JunitPlayGround.junit5playground.rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;

@RestController
public class ApiController {


    @GetMapping("/ais/refresh")
    public String getSomething() throws HttpTimeoutException {
        System.out.println("API has been called " + LocalDateTime.now());
        return "HELLO";
    }
}
