package com.example.JunitPlayGround.junit5playground.rest;


import com.example.JunitPlayGround.junit5playground.model.ApiRequest;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;

@RestController
public class ApiController {


    @GetMapping("/ais/{id}/refresh/{id2}/hello")
    public String getSomething(@PathVariable String id, @PathVariable String id2, @RequestParam String id3) throws HttpTimeoutException {
        System.out.println("API has been called " + LocalDateTime.now());
        return "HELLO";
    }


    @PostMapping("/ais/{id}/create")
    public String postSomething(@PathVariable String id, @RequestBody ApiRequest apiRequest) throws HttpTimeoutException {
        System.out.println("API has been called " + LocalDateTime.now());
        return "HELLO";
    }
}
