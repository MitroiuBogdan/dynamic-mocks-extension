package com.example.JunitPlayGround.junit5playground.extension_3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonUtils {

    private static ObjectMapper createObjMapper() {
        return new ObjectMapper();
    }

    //Replace in response






    //Extract values from URL
    private List<String> extractParametersFromURL(String url, String urlRegex) {
        List<String> urlParameters = new ArrayList<>();
        Matcher matchResult = Pattern.compile(urlRegex).matcher(url);
        if (matchResult.matches()) {
            int index = 1;
            while (index <= matchResult.groupCount()) {
                urlParameters.add(matchResult.group(index));
                index++;
            }
        }
        return urlParameters;
    }

    //Extract values from Regex URL
    private List<String> extractParameterNamesFromRegexURL(String urlRegex, String pattern) {
        return Pattern.compile(pattern)
                .matcher(urlRegex)
                .results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toList());
    }

    //Extract value from request
    public Map<String, String> extractFieldValueFromJsonString(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> finalResult = new HashMap<>();
        objectMapper
                .readValue(jsonString, Map.class)
                .forEach((key, value) -> extractFieldValueFromNode(finalResult, String.valueOf(key), value));

        return finalResult;
    }

    private Map<String, String> extractFieldValueFromNode(Map<String, String> finalResult, String nodeKey, Object nodeValue) {
        if (nodeValue instanceof Map) {
            ((Map<?, ?>) nodeValue).forEach((key, value) ->
                    extractFieldValueFromNode(finalResult, appendParentKeyName(nodeKey, String.valueOf(key)), value));
        } else {
            String value = null == nodeValue ? "null" : String.valueOf(nodeValue);
            finalResult.put(String.valueOf(nodeKey), value);
        }
        return finalResult;
    }

    private String appendParentKeyName(String parentKeyNode, String nodeKey) {
        return new StringBuilder()
                .append(parentKeyNode)
                .append(".")
                .append(nodeKey)
                .toString();
    }


}
