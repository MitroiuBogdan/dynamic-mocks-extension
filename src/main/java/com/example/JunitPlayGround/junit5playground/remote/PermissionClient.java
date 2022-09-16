package com.example.JunitPlayGround.junit5playground.remote;

import com.example.JunitPlayGround.junit5playground.model.PermissionPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class PermissionClient {


    CompletableFuture<PermissionPOJO> callAPI(String id, String message) {
        return CompletableFuture.supplyAsync(() ->
        {
            if (id == null || message == null) {
                throw new RuntimeException("null permission");

            } else {
             return  new PermissionPOJO(id, message);
            }

        });

    }
}
