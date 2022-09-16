package com.example.JunitPlayGround.junit5playground.remote;

import com.example.JunitPlayGround.junit5playground.model.Grant;
import com.example.JunitPlayGround.junit5playground.model.PermissionPOJO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PermissionRemoteService {


    @Autowired
    PermissionClient permissionClient;

    public CompletableFuture<PermissionPOJO> createPermission(String id, String message) {
        return permissionClient
                .callAPI(id, message)
//                .whenCompleteAsync((permissionPOJO, throwable) -> {
//                    Optional.ofNullable(throwable).ifPresent(throwable1 -> {
//                        System.out.println("DO AN EXTERNAL CALL");
//                    });
//                })
                .thenApplyAsync(permissionPOJO -> permissionPOJO)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    System.out.println("NULL PERMISSIONS");
                    throw new RuntimeException("Layer 2");
                });
//                .thenApplyAsync(permissionPOJO ->
//                {
//                    System.out.println(permissionPOJO);
//                    return permissionPOJO;
//                });
//                .handleAsync((permissionPOJO, throwable) -> Optional.ofNullable(permissionPOJO)
//                        .map(permissionPOJO1 -> {
//                            System.out.println("LOGICX");
//                            return permissionPOJO1;
//                        }).orElseThrow(() -> {
//                            System.out.println("LOGIX on throw");
//                            return null;
//                        }));

    }
}