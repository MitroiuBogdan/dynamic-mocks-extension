package com.ing.casy.extensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.regex.Pattern.compile;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Slf4j
public class YlluxBodyTransformer extends ResponseDefinitionTransformer {

    private static final Pattern RESPONSE_FIELD_PATTERN = Pattern.compile("\\$\\(.*?\\)"); // e.g. $(id)
    private static final Pattern REGEX_URL_PATTERN = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*?)>");// e.g. (?<id>.*?)
    private static final String RANDOM_UUID_PATTERN = "$(@randomUuid)";

    private static final String TRANSFORMER_NAME = "casy-body-transformer";
    private static final String URL_REGEX = "urlRegex";
    private ObjectMapper objectMapper;


    public YlluxBodyTransformer() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public String getName() {
        return TRANSFORMER_NAME;
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters transformerParameters) {
        if (this.hasEmptyResponseBody(responseDefinition)) {
            return responseDefinition;
        }
        String urlRegex = Optional
                .of(transformerParameters.getString(URL_REGEX))
                .orElseThrow(() -> new RuntimeException("urlRegex cannot be null"));

        Map<String, String> requestParametersContext = storeParametersFromURL(request.getUrl(), urlRegex);
        Map<String, String> urlParametersContext = storeFieldsFromRequestBody(request);

        Map<String, String> context = mergeContexts(urlParametersContext, requestParametersContext);

        String responseBody = getResponseBody(responseDefinition, fileSource);
        String modifiedResponseBody = replaceResponseFieldsWithValuesFromContext(responseBody, context);

        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withBody(modifiedResponseBody)
                .build();
    }

    private Map<String, String> mergeContexts(Map<String, String> map1, Map<String, String> map2) {
        return Stream.of(map1, map2)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    private boolean hasEmptyResponseBody(ResponseDefinition responseDefinition) {
        return responseDefinition.getBody() == null && responseDefinition.getBodyFileName() == null;
    }

    private Map<String, String> storeFieldsFromRequestBody(Request request) {
        String requestBody = request.getBodyAsString();
        if (!Strings.isEmpty(requestBody)) {
            try {
                ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(requestBody);
                Map<String, String> map = extractJsonFieldValueIntoMap(new HashMap<>(), jsonNode, null, 0);
                return map;
            } catch (JsonProcessingException e) {
                log.warn("Request body is empty");
            }
        }
        return new HashMap<>();
    }

    private Map<String, String> storeParametersFromURL(String url, String urlRegex) {
        List<String> keys = extractParameterNamesFromRegexURL(urlRegex);
        List<String> values = extractParameterValuesFormURL(url, urlRegex);

        Map<String, String> map = new HashMap<>();
        if (keys.size() == values.size()) {
            for (int i = 0; i < keys.size(); i++) {
                map.put(keys.get(i), values.get(i));
            }
        } else {
            System.out.println("Values did match");
        }
        return map;
    }

    private List<String> extractParameterNamesFromRegexURL(String urlRegex) {
        return REGEX_URL_PATTERN
                .matcher(urlRegex)
                .results()
                .map(matchResult -> matchResult.group(1))
                .collect(Collectors.toList());
    }

    private List<String> extractParameterValuesFormURL(String url, String urlRegex) {
        Matcher matcher = compile(urlRegex)
                .matcher(url);
        matcher.matches();

        List<String> values = new ArrayList<>();
        for (int i = 1; i < matcher.groupCount() + 1; i++) {
            values.add(matcher.group(i));
        }
        return values;
    }

    private String getResponseBody(ResponseDefinition responseDefinition, FileSource fileSource) {
        String body;
        if (responseDefinition.getBody() != null) {
            body = responseDefinition.getBody();
        } else {
            BinaryFile binaryFile = fileSource.getBinaryFileNamed(responseDefinition.getBodyFileName());
            body = new String(binaryFile.readContents(), StandardCharsets.UTF_8);
        }
        return body;
    }

    private String replaceResponseFieldsWithValuesFromContext(String responseBodyRaw, Map<String, String> context) {
        String modifiedResponseBody = responseBodyRaw;
        List<String> responseFields = RESPONSE_FIELD_PATTERN.matcher(responseBodyRaw)
                .results()
                .map(MatchResult::group)
                .collect(Collectors.toList());

        for (String regexField : responseFields) {
            if (regexField.equals(RANDOM_UUID_PATTERN)) {
                System.out.println(regexField);
                modifiedResponseBody = modifiedResponseBody.replace(regexField, UUID.randomUUID().toString());
            }

            if (!context.containsKey(regexField.substring(2, regexField.length() - 1))) {
                modifiedResponseBody = modifiedResponseBody.replace(regexField, "null");
            } else
                modifiedResponseBody = modifiedResponseBody.replace(regexField, getValueForContext(regexField, context));
        }
        return modifiedResponseBody;
    }

    private String getValueForContext(String field, Map<String, String> transformerContext) {
        String fieldName = field.substring(2, field.length() - 1);
        return transformerContext.get(fieldName);
    }


    private Map<String, String> extractJsonFieldValueIntoMap(Map<String, String> resultMap, JsonNode node, String parentNodeName, int layerIndex) throws JsonProcessingException {
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> field = it.next();
            if (!field.getValue().isValueNode() && !field.getValue().isArray()) {
                String fieldName = parentNodeName != null ? appendParentNodeName(parentNodeName, field.getKey()) : field.getKey();
                extractJsonFieldValueIntoMap(resultMap, field.getValue(), fieldName, layerIndex);
            } else {
                String resultMapKey = appendParentNodeName(parentNodeName, field.getKey());
                String resultMapValue;
                if (field.getValue().isNumber()) {
                    resultMapValue = field.getValue().toString();
                } else if (field.getValue().isArray()) {
                    resultMapValue = field.getValue() != null ? String.valueOf(field.getValue().toString()) : "null";
                } else {
                    resultMapValue = !isBlank(field.getValue().textValue()) ? field.getValue().textValue() : "null";
                }
                resultMap.put(resultMapKey, resultMapValue);
                layerIndex++;
            }
        }
        return resultMap;
    }

    private String appendParentNodeName(String parentNodeName, String nodeName) {
        StringBuilder stringBuilder = new StringBuilder();
        String fieldName;
        if (parentNodeName != null) {
            fieldName = stringBuilder.append(parentNodeName)
                    .append(".")
                    .append(nodeName).toString();
        } else {
            fieldName = nodeName;
        }
        return fieldName;
    }
}
