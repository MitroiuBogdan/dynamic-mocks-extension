package com.example.JunitPlayGround.junit5playground.model;

public class ApiResponse {

    private int responseId;
    private String responseMessage;

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ApiResponse(int responseId, String responseMessage) {
        this.responseId = responseId;
        this.responseMessage = responseMessage;
    }
}
