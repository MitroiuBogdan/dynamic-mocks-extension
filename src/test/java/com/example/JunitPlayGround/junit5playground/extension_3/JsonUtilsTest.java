package com.example.JunitPlayGround.junit5playground.extension_3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class JsonUtilsTest {

    ObjectMapper objectMapper = new ObjectMapper();


    static final Pattern RESPONSE_FIELD_PATTERN = Pattern.compile("<.*?>"); // e.g. $(id)
    static final Pattern RESPONSE_RANDOM = Pattern.compile("%random%");

    @Test
    void testJsonUtils() throws JsonProcessingException {
        Map<String, String> context = createTestMap();
        String response = getResponseString();
        print(response);
        print(context);

        String str = "<p1>";

        //Replace first occurrence of substring "Java" with "JAVA"
//        String newStr = str.replaceFirst("<p1>", "soupe");
//
//        System.out.println(newStr);

//        String value = "<p1>";
//        boolean m = RESPONSE_FIELD_PATTERN
//                .matcher(value)
//                .matches();
//
//        if (m) {
//            System.out.println("something");
//            response = response.replaceFirst(value, context.get(value.substring(1, value.length() - 1)));
//            System.out.println(response);
//        }

        String finalValue = replaceJson(response, context);
        System.out.println(finalValue);

    }


    public String replaceJson(String jsonString, Map<String, String> context) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jsonString = replaceValue(jsonString, context, entry.getValue());

        }
        return jsonString;
    }

    private String replaceValue(String replacedJsonString, Map<String, String> context, Object nodeValue) {
        if (nodeValue instanceof Map) {
            String finalReplacedJsonString = replacedJsonString;
            ((Map<?, ?>) nodeValue).forEach((key, value) ->
                    replaceValue(finalReplacedJsonString, context, value)
            );
        } else {
            String nodeValueString = String.valueOf(nodeValue);
            boolean isReplaceableField = RESPONSE_FIELD_PATTERN.matcher(nodeValueString).matches();
            boolean isRandomUUIDField = RESPONSE_RANDOM.matcher(nodeValueString).matches();

            if (isRandomUUIDField) {
                replacedJsonString = replacedJsonString.replaceFirst(nodeValueString, "123-123-123-123-123");
            }

            if (isReplaceableField) {
                if (!context.containsKey(removeRegexSyntax(nodeValueString))) {
                    replacedJsonString = replacedJsonString.replaceFirst("\"" + nodeValueString + "\"", "null");
                } else {
                    replacedJsonString = replacedJsonString.replaceFirst(nodeValueString, context.get(removeRegexSyntax(nodeValueString)));
                }
            }
        }
        return replacedJsonString;
    }

    private String removeRegexSyntax(String s) {
        return s.substring(1, s.length() - 1);
    }


    public Map<String, String> createTestMap() {
        return Map.of(
                "p1", "Kim",
                "p2", "Lisa",
                "q1", "Hyuna"
        );
    }

    public String getResponseString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                new JsonResponse(
                        "<p1>",
                        "%random%",
                        "<p19>"
                )
        );
    }

    class JsonResponse {

        public String name_1;
        public String name_2;
        public String name_3;

        public JsonResponse(String name_1, String name_2, String name_3) {
            this.name_1 = name_1;
            this.name_2 = name_2;
            this.name_3 = name_3;
        }

        public String getName_1() {
            return name_1;
        }

        public String getName_2() {
            return name_2;
        }

        public String getName_3() {
            return name_3;
        }
    }

    //    DEV
    public void print(Object o) {
        System.out.println(o);
    }
}