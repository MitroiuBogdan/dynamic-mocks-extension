package com.example.JunitPlayGround.junit5playground.initializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

public class YlluTransformer extends ResponseDefinitionTransformer {

    public final String TRANSFORMER_NAME = "yllu-transformer";
    public ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getName() {
        return TRANSFORMER_NAME;
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {
        if (expectEmptyResponse(responseDefinition)) {
            return responseDefinition;
        }
        String requestBody = request.getBodyAsString();
        Map object = null;
        if (!Strings.isEmpty(requestBody)) {
            try {
                object = objectMapper.readValue(requestBody, Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        String urlRegex;
        if (parameters != null) {
            urlRegex = parameters.getString("urlRegex");
            if (urlRegex != null) {
                Pattern p = Pattern.compile(urlRegex);
                Matcher m = p.matcher(request.getUrl());

                List<String> groups = extractParametersFromURL(urlRegex);
//
//                if (m.matches() && groups.size() > 0 && groups.size() <= m.groupCount()) {
//                    for (i = 0; i < groups.size(); ++i) {
//                        if (object == null) {
//                            object = new HashMap();
//                        }
//
//                        ((Map) object).put(groups.get(i), m.group(i + 1));
//                    }
//                }
            }
        }
        String responseBody = getResponseBody(responseDefinition, fileSource);

        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withBodyFile(null)
                .withBody("dsa")
                .build();
    }

    private static String getResponseBody(ResponseDefinition responseDefinition, FileSource fileSource) {
        String body;
        if (responseDefinition.getBody() != null) {
            body = responseDefinition.getBody();
        } else {
            BinaryFile binaryFile = fileSource.getBinaryFileNamed(responseDefinition.getBodyFileName());
            body = new String(binaryFile.readContents(), StandardCharsets.UTF_8);
        }

        return body;
    }

    private static boolean hasSamePattern(String patternUrl, String regexUrl) {
        return compile(regexUrl)
                .matcher(patternUrl)
                .matches();
    }

    private static List<String> extractParametersFromURL(String regex) {
        return compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*?)>")
                .matcher(regex)
                .results()
                .map(matchResult -> {
                    System.out.println(matchResult.group(1));
                    return matchResult.group(1);

                })
                .collect(Collectors.toList());
    }

    private boolean expectEmptyResponse(ResponseDefinition responseDefinition) {
        return !StringUtils.hasLength(responseDefinition.getBody()) && !StringUtils.hasLength(responseDefinition.getBodyFileName());
    }


}
