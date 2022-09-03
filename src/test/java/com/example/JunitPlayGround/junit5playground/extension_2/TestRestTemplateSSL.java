package com.example.JunitPlayGround.junit5playground.extension_2;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

public class TestRestTemplateSSL extends TestRestTemplate {

    public TestRestTemplateSSL(HttpClientOption... httpClientOptions) {
        super(httpClientOptions);
    }

    @PostConstruct
    public void setSSLContext() {
        ((HttpComponentsClientHttpRequestFactory) super.getRestTemplate().getRequestFactory()).setConnectTimeout(5000);
        System.out.println("SUCCESS");
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return super.exchange(requestEntity, responseType);
    }
}
