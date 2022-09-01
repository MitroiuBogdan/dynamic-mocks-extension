package com.example.JunitPlayGround.junit5playground.model;

public class ApiRequest {

    private int id;
    private String message;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiRequest(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
