package com.example.JunitPlayGround.junit5playground.rest;


import com.example.JunitPlayGround.junit5playground.model.ApiRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;

@RestController
public class ApiController {

    private final RestTemplate restController;
    private final ProviderRepo providerRepo;

    public ApiController(ProviderRepo providerRepo) {
        this.providerRepo = providerRepo;
        this.restController = new RestTemplate();
    }

    @GetMapping("/ais/{id}/refresh/{id2}/hello")
    public ResponseEntity getSomething(@PathVariable String id, @PathVariable String id2, @RequestParam String id3) throws HttpTimeoutException {
        System.out.println("API has been called " + LocalDateTime.now());
        providerRepo.add("123");
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/ais/{id}/create")
    public String postSomething(@PathVariable String id, @RequestBody ApiRequest apiRequest) throws HttpTimeoutException {
        System.out.println("API has been called " + LocalDateTime.now());
        System.out.println("API has been called " + LocalDateTime.now());
        providerRepo.add("000");
        return "HELLO";
    }

    public void increment(String f) {
        providerRepo.add("HAcked");
    }

    public void inside() {
        System.out.println("inside "+ providerRepo.getList());
    }
}
