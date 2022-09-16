package com.example.JunitPlayGround.junit5playground.rest;


import com.example.JunitPlayGround.junit5playground.model.ApiRequest;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpTimeoutException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProviderRepo {

    private final List<String> list = new ArrayList<>();

    public void ProviderRepo() {

    }


    public void add(String s) {
        list.add(s);
    }

    public List<String> getList() {
        return list;
    }
}
