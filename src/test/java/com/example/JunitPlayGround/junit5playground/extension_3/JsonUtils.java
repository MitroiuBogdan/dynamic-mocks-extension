package com.example.JunitPlayGround.junit5playground.extension_3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtils {

    private static ObjectMapper createObjMapper() {
        return new ObjectMapper();
    }


    public static Map extractJsonFieldValue(String jsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = createObjMapper();
        Map object = objectMapper.readValue(jsonStr, Map.class);
        return object;
    }


}
