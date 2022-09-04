package com.example.JunitPlayGround.junit5playground.transformer;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
public class YlluResponseTransformer extends ResponseDefinitionTransformer {

    private static final String TRANSFORMER_NAME = "casy-response-transformer";
    private static final String URL_REGEX = "urlRegex";

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
        if (hasEmptyResponseBody(responseDefinition)) {
            return responseDefinition;
        }
        Map<String, Object> requestParametersContext = cacheParametersFromUrl(request, transformerParameters);
        Map<String, Object> urlParametersContext = cacheFieldsFromRequest(request);

        Map<String, Object> context = ResponseTransformerUtils.mergeContexts(urlParametersContext, requestParametersContext);

        String responseBody = getResponseBody(responseDefinition, fileSource);
        String modifiedResponseBody = ResponseTransformerUtils.replaceResponseRegexFieldsWithContextValues(responseBody, context);
        System.out.println(modifiedResponseBody);
        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withBody(modifiedResponseBody)
                .build();
    }

    private boolean hasEmptyResponseBody(ResponseDefinition responseDefinition) {
        return responseDefinition.getBody() == null && responseDefinition.getBodyFileName() == null;
    }

    private Map<String, Object> cacheFieldsFromRequest(Request request) {
        String requestBody = request.getBodyAsString();
        return ResponseTransformerUtils.extractFieldValueFromJsonRequest(requestBody);
    }

    private Map<String, Object> cacheParametersFromUrl(Request request, Parameters transformerParameters) {
        String urlRegex = Optional
                .of(transformerParameters.getString(URL_REGEX))
                .orElseThrow(() -> new RuntimeException("urlRegex cannot be null"));

        String url = Optional.of(request.getUrl())
                .orElseThrow(() -> new RuntimeException("Url cannot be null"));

        List<String> parameterName = ResponseTransformerUtils.extractParameterNamesFromRegexUrl(urlRegex);
        List<String> parameterValue = ResponseTransformerUtils.extractParametersFromUrl(url, urlRegex);

        Map<String, Object> parameterNameValueMap = new HashMap<>();
        if (parameterName.size() == parameterValue.size()) {
            for (int i = 0; i < parameterName.size(); i++) {
                parameterNameValueMap.put(parameterName.get(i), parameterValue.get(i));
            }
        } else {
            System.out.println("Values found in url didn't match urlRegex");
        }
        return parameterNameValueMap;
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

}