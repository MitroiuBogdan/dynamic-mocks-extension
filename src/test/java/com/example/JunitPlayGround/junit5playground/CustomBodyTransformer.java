package com.example.JunitPlayGround.junit5playground;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.BinaryFile;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomBodyTransformer extends ResponseDefinitionTransformer {
    private static final Pattern interpolationPattern = Pattern.compile("\\$\\(.*?\\)");
    private static final Pattern randomIntegerPattern = Pattern.compile("!RandomInteger");
    private static ObjectMapper jsonMapper = initJsonMapper();
    private static ObjectMapper xmlMapper = initXmlMapper();

    public void BodyTransformer() {
    }

    private static ObjectMapper initJsonMapper() {
        return new ObjectMapper();
    }

    private static ObjectMapper initXmlMapper() {
        JacksonXmlModule configuration = new JacksonXmlModule();
        configuration.setXMLTextElementName("value");
        return new XmlMapper(configuration);
    }

    public boolean applyGlobally() {
        return false;
    }

    private String transformResponse(Map requestObject, String response) {
        String modifiedResponse = response;

        String group;
        for(Matcher matcher = interpolationPattern.matcher(response); matcher.find(); modifiedResponse = modifiedResponse.replace(group, this.getValue(group, requestObject))) {
            group = matcher.group();
        }

        return modifiedResponse;
    }

    private CharSequence getValue(String group, Map requestObject) {
        return (CharSequence)(randomIntegerPattern.matcher(group).find() ? String.valueOf((new Random()).nextInt(2147483647)) : this.getValueFromRequestObject(group, requestObject));
    }

    private CharSequence getValueFromRequestObject(String group, Map requestObject) {
        String fieldName = group.substring(2, group.length() - 1);
        String[] fieldNames = fieldName.split("\\.");
        Object tempObject = requestObject;
        String[] var6 = fieldNames;
        int var7 = fieldNames.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String field = var6[var8];
            if (tempObject instanceof Map) {
                tempObject = ((Map)tempObject).get(field);
            }
        }

        return String.valueOf(tempObject);
    }

    private boolean hasEmptyResponseBody(ResponseDefinition responseDefinition) {
        return responseDefinition.getBody() == null && responseDefinition.getBodyFileName() == null;
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

    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {
        if (this.hasEmptyResponseBody(responseDefinition)) {
            return responseDefinition;
        } else {
            Map object = null;
            String requestBody = request.getBodyAsString();

            int i;
            try {
                object = (Map)jsonMapper.readValue(requestBody, Map.class);
            } catch (IOException var17) {
                try {
                    object = (Map)xmlMapper.readValue(requestBody, Map.class);
                } catch (IOException var16) {
                    String[] pairedValues;
                    int var12;
                    if (StringUtils.isNotEmpty(requestBody) && (requestBody.contains("&") || requestBody.contains("="))) {
                        object = new HashMap();
                       pairedValues = requestBody.split("&");
                        pairedValues = pairedValues;
                        i = pairedValues.length;

                        for(var12 = 0; var12 < i; ++var12) {
                            String pair = pairedValues[var12];
                            String[] values = pair.split("=");
                            ((Map)object).put(values[0], values.length > 1 ? this.decodeUTF8Value(values[1]) : "");
                        }
                    } else if (request.getAbsoluteUrl().split("\\?").length == 2) {
                        object = new HashMap();
                        String absoluteUrl = request.getAbsoluteUrl();
                        pairedValues = absoluteUrl.split("\\?")[1].split("&");
                        String[] var11 = pairedValues;
                        var12 = pairedValues.length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                            String pair = var11[var13];
                            String[] values = pair.split("=");
                            ((Map)object).put(values[0], values.length > 1 ? this.decodeUTF8Value(values[1]) : "");
                        }
                    } else {
                        System.err.println("[Body parse error] The body doesn't match any of 3 possible formats (JSON, XML, key=value).");
                    }
                }
            }

            String urlRegex;
            if (parameters != null) {
                urlRegex = parameters.getString("urlRegex");
                if (urlRegex != null) {
                    Pattern p = Pattern.compile(urlRegex);
                    Matcher m = p.matcher(request.getUrl());
                    List<String> groups = getNamedGroupCandidates(urlRegex);
                    if (m.matches() && groups.size() > 0 && groups.size() <= m.groupCount()) {
                        for(i = 0; i < groups.size(); ++i) {
                            if (object == null) {
                                object = new HashMap();
                            }

                            ((Map)object).put(groups.get(i), m.group(i + 1));
                        }
                    }
                }
            }

            urlRegex = this.getResponseBody(responseDefinition, fileSource);
            return ResponseDefinitionBuilder.like(responseDefinition).but().withBodyFile((String)null).withBody(this.transformResponse((Map)object, urlRegex)).build();
        }
    }

    private static List<String> getNamedGroupCandidates(String regex) {
        List<String> namedGroups = new ArrayList();
        Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*?)>").matcher(regex);

        while(m.find()) {
            namedGroups.add(m.group(1));
        }

        return namedGroups;
    }

    private String decodeUTF8Value(String value) {
        String decodedValue = "";

        try {
            decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException var4) {
            System.err.println("[Body parse error] Can't decode one of the request parameter. It should be UTF-8 charset.");
        }

        return decodedValue;
    }

    public String getName() {
        return "body-transformer";
    }

}