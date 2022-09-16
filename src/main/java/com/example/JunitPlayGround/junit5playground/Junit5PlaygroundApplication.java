package com.example.JunitPlayGround.junit5playground;

import com.example.JunitPlayGround.junit5playground.remote.ActivateRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Junit5PlaygroundApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Junit5PlaygroundApplication.class, args);
    }

    @Autowired
    ActivateRemoteService activateRemoteService;

    @Override
    public void run(String... args) throws Exception {

        activateRemoteService.createPermission();


    }
}
