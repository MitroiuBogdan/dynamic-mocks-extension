package com.example.JunitPlayGround.junit5playground.extension_3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class JsonUtilsTest {

    ObjectMapper objectMapper = new ObjectMapper();

    String urlRegex = "/ais/(?<id>.*?)/refresh/(?<id2>.*?)/hello\\?id3=(?<id3>.*?)&id4=(?<id4>.*?)";
    String url = "/ais/*.[^/]+/refresh/*.[^/]+/hello";

    static final Pattern RESPONSE_FIELD_PATTERN = Pattern.compile("\\$\\(.*?\\)"); // e.g. $(id)
    static final String RANDOM_UUID_PATTERN = "$(@randomUuid)";
    static final String URL_REGEX_PATTERN = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*?)>";


    @Test
    void testJsonUtils() throws JsonProcessingException {

        String urlTest = "/ais/123/refresh/001/hello?id3=0003&id4=0009";
        String url = "/ais/*.[^/]+/refresh/*.[^/]+/hello?id3=*.[^/]+";

        List<String> parameters = extractParametersFromURL(urlTest, urlRegex);

        System.out.println(parameters);
    }

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

    private List<String> extractParameterNamesFromRegexURL(String urlRegex,String regexUrlParameterPattern) {
        return Pattern.compile(regexUrlParameterPattern)
                .matcher(urlRegex)
                .results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toList());
    }

    //    DEV
    public void print(Object o) {
        System.out.println(o);
    }
}