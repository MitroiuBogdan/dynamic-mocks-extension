package com.example.JunitPlayGround.junit5playground.remote;

import com.example.JunitPlayGround.junit5playground.model.Grant;
import com.example.JunitPlayGround.junit5playground.model.PermissionPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ActivateRemoteService {


    @Autowired
    PermissionRemoteService permissionRemoteService;


    public CompletableFuture<Grant> createPermission() {
        return permissionRemoteService.createPermission(null, "2")
//                .handleAsync((permissionPOJO, throwable) -> {
//                    if (throwable != null) {
//                        System.out.println("Bad");
//                        throw new RuntimeException("xxx");
//                    } else {
//                        System.out.println("ACTIVATE");
//                    System.out.println(permissionPOJO);
//                    if (true) {
//                        throw new RuntimeException("HAHA");
//                    }
//                    return new Grant(permissionPOJO.getId(), permissionPOJO.getMessage());
//                    }
//                });

                .whenCompleteAsync((permissionPOJO, throwable) -> {
                    Optional.ofNullable(throwable)
                            .ifPresent(throwable1 -> System.out.println("XXX"));
                }).thenApplyAsync(permissionPOJO -> {
                    System.out.println("ACTIVATE");
                    System.out.println(permissionPOJO);
                    if (true) {
                        throw new RuntimeException("HAHA");
                    }
                    return new Grant(permissionPOJO.getId(), permissionPOJO.getMessage());
                });


    }
}
