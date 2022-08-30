package com.example.JunitPlayGround.junit5playground.rest;


import org.springframework.web.bind.annotation.*;

import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;

@RestController
public class ApiController {


    @GetMapping("/ais/{id}/refresh/{id2}")
    public String getSomething(@PathVariable String id, @PathVariable String id2) throws HttpTimeoutException {
        System.out.println("API has been called " + LocalDateTime.now());
        return "HELLO";
    }
}
