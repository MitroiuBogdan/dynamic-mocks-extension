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
//        String jsonStr = extractStringValue();
//        print(jsonStr);


//        Map<String, Object> map = extractFieldValueFromJsonString(jsonStr);

//        print(map);
    }

//    DEV

    public Map<String, Object> extractFieldValueFromJsonString(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> finalResult = new HashMap<>();
        objectMapper
                .readValue(jsonString, Map.class)
                .forEach((key, value) -> extractFieldValueFromNode(finalResult, String.valueOf(key), value));

        return finalResult;
    }

    public Map<String, Object> extractFieldValueFromNode(Map<String, Object> finalResult, String nodeKey, Object nodeValue) {
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
        StringBuilder stringBuilder = new StringBuilder();
        String finalKeyName;
        if (parentKeyNode != null) {
            finalKeyName = stringBuilder.append(parentKeyNode)
                    .append(".")
                    .append(nodeKey).toString();
        } else {
            finalKeyName = nodeKey;
        }
        return finalKeyName;
    }

    //
    public String extractStringValue() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new DataRequest(testClass()));
    }

    public TestClass testClass() {
        return new TestClass(
                "name",
                null,
                new Temp(null, 100D, List.of("Lisa", "Jenny")),
                List.of("Anne", "Mirabel"));
    }

    class DataRequest {
        private TestClass data;

        public DataRequest(TestClass data) {
            this.data = data;
        }

        public TestClass getData() {
            return data;
        }

        public void setData(TestClass data) {
            this.data = data;
        }
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