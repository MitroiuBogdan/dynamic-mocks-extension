package com.example.JunitPlayGround.junit5playground.transformer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;


public class ResponseTransformerUtils {

    static final String RESPONSE_FIELD_PATTERN = "<.*?>"; // e.g. "id":"<id>"
    static final String RESPONSE_RANDOM_UUID_PATTERN = "%UUID%"; //e.g "id":"%rUUID%"
    static final String REGEX_URL_PATTERN = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*?)>"; //e.g ais/(?<id>)/refresh?id2=(?<id2>)

    public static Map<String, Object> mergeContexts(Map<String, Object> map1, Map<String, Object> map2) {
        return Stream.of(map1, map2)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    public static List<String> extractParametersFromUrl(String url, String urlRegex) {
        List<String> urlParameters = new ArrayList<>();
        Matcher matchResult = compile(urlRegex).matcher(url);
        if (matchResult.matches()) {
            int index = 1;
            while (index <= matchResult.groupCount()) {
                urlParameters.add(matchResult.group(index));
                index++;
            }
        }
        return urlParameters;
    }

    public static List<String> extractParameterNamesFromRegexUrl(String urlRegex) {
        return compile(REGEX_URL_PATTERN)
                .matcher(urlRegex)
                .results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toList());
    }

    public static Map<String, Object> extractFieldValueFromJsonRequest(String jsonRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> finalResult = new HashMap<>();
        try {
            objectMapper.readValue(jsonRequest, Map.class)
                    .forEach((key, value) -> extractFieldValueFromJsonNode(finalResult, String.valueOf(key), value));
        } catch (Exception e) {
            System.out.println("extractFieldValueFromJsonString - "+e.getMessage());
        }
        return finalResult;
    }

    private static Map<String, Object> extractFieldValueFromJsonNode(Map<String, Object> finalResult, String nodeKey, Object nodeValue) {
        if (nodeValue instanceof Map) {
            ((Map<?, ?>) nodeValue).forEach((key, value) ->
                    extractFieldValueFromJsonNode(finalResult, appendParentKeyName(nodeKey, String.valueOf(key)), value));
        } else {
            String value = null == nodeValue ? "null" : (String) nodeValue;
            finalResult.put(String.valueOf(nodeKey), value);
        }
        return finalResult;
    }

    private static String appendParentKeyName(String parentKeyNode, String nodeKey) {
        return new StringBuilder()
                .append(parentKeyNode)
                .append(".")
                .append(nodeKey)
                .toString();
    }

    public static String replaceResponseRegexFieldsWithContextValues(String body, Map<String, Object> context) {
        List<String> responseFieldsPatterns = findPatterns(body, RESPONSE_FIELD_PATTERN);
        List<String> randomUUIDPatterns = findPatterns(body, RESPONSE_RANDOM_UUID_PATTERN);

        List<String> replaceablePatternsFromResponse = Stream.of(responseFieldsPatterns, randomUUIDPatterns)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        for (String fieldPattern : replaceablePatternsFromResponse) {
            body = replaceFieldPatternWithContextValue(body, fieldPattern, context);
        }
        return body;
    }

    private static String replaceFieldPatternWithContextValue(String body, String fieldPattern, Map<String, Object> context) {
        boolean isReplaceableField = compile(RESPONSE_FIELD_PATTERN).matcher(fieldPattern).matches();
        boolean isRandomUUIDField = compile(RESPONSE_RANDOM_UUID_PATTERN).matcher(fieldPattern).matches();
        /*Add new pattern here*/
        if (isRandomUUIDField) {
            return body.replaceFirst(fieldPattern, UUID.randomUUID().toString());
        }
        if (isReplaceableField) {
            if (!context.containsKey(removeRegexSyntax(fieldPattern))) {
                return body.replace("\"" + fieldPattern + "\"", "null");
            } else {
                Object value = context.get(removeRegexSyntax(fieldPattern));
                if (value instanceof String) {
                    return body.replace(fieldPattern, String.valueOf(value));
                } else if (value instanceof Collection) {
                    return body.replaceFirst("\"" + fieldPattern + "\"", generateStringValueForArray((Collection) value));
                } else {
                    return body.replace("\"" + fieldPattern + "\"", String.valueOf(value));
                }
            }
        }
        return body;
    }

    private static List<String> findPatterns(String target, String pattern) {
        return Pattern.compile(pattern).
                matcher(target)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());
    }

    private static String generateStringValueForArray(Collection collection) {
        Object[] objectArray = collection.toArray();
        if (objectArray.length > 0 && objectArray[0] instanceof String) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < objectArray.length; i++) {
                if (i < objectArray.length - 1) {
                    sb.append("\"" + objectArray[i] + "\",");
                } else {
                    sb.append("\"" + objectArray[i] + "\"");
                }
            }
            sb.append("]");
            return sb.toString();
        } else {
            return String.valueOf(collection);
        }
    }

    private static String removeRegexSyntax(String s) {
        return s.substring(1, s.length() - 1);
    }

}
