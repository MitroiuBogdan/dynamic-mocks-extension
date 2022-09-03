package com.example.JunitPlayGround.junit5playground.extension_3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JsonUtilsTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonUtils() throws JsonProcessingException {
        String jsonStr = extractStringValue();
        print(jsonStr);

        Map<String, Object> finalResult = JsonUtils.extractJsonFieldValue(jsonStr);
        Map<String, Object> mergedMap = new HashMap();
        finalResult.forEach((nodeKey, nodeValue) -> {
            extractFieldsFromNLayer(mergedMap, nodeKey, nodeValue);

        });

        print(mergedMap);
    }

//    DEV

    public Map<String, Object> extractFieldsFromNLayer(Map<String, Object> finalResult, Object nodeKey, Object nodeValue) {
        if (nodeValue instanceof Map) {
            ((Map<?, ?>) nodeValue).forEach((key, value) -> {
                extractFieldsFromNLayer(finalResult, key, value);
            });
        } else {
            finalResult.put(String.valueOf(nodeKey), nodeValue);
        }
        return finalResult;
    }

    public String appendParentKeyName(Object parenKey) {

        return null;
    }

    //
    public String extractStringValue() throws JsonProcessingException {
        return objectMapper.writeValueAsString(testClass());
    }

    public TestClass testClass() {
        return new TestClass(
                "name",
                10D,
                new Temp("nestedName", 100D, List.of("Lisa", "Jenny")),
                List.of("Anne", "Mirabel"));
    }

    class TestClass {

        public String name;
        public Double value;
        public Temp temp;
        public List<String> stringList;

        public TestClass(String name, Double value, Temp temp, List<String> stringList) {
            this.name = name;
            this.value = value;
            this.temp = temp;
            this.stringList = stringList;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public Temp getTemp() {
            return temp;
        }

        public void setTemp(Temp temp) {
            this.temp = temp;
        }

        public List<String> getStringList() {
            return stringList;
        }

        public void setStringList(List<String> stringList) {
            this.stringList = stringList;
        }
    }

    class Temp {
        public String tempName;
        public Double tempValue;
        public List tempListString;

        public Temp(String tempName, Double tempValue, List tempListString) {
            this.tempName = tempName;
            this.tempValue = tempValue;
            this.tempListString = tempListString;
        }

        public String getTempName() {
            return tempName;
        }

        public void setTempName(String tempName) {
            this.tempName = tempName;
        }

        public Double getTempValue() {
            return tempValue;
        }

        public void setTempValue(Double tempValue) {
            this.tempValue = tempValue;
        }

        public List getTempListString() {
            return tempListString;
        }

        public void setTempListString(List tempListString) {
            this.tempListString = tempListString;
        }
    }

    public void print(Object o) {
        System.out.println(o);
    }
}