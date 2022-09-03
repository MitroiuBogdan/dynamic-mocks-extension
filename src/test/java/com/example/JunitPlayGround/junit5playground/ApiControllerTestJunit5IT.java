package com.example.JunitPlayGround.junit5playground;

import com.example.JunitPlayGround.junit5playground.extension_2.WiremockCustom;
import com.example.JunitPlayGround.junit5playground.model.ApiRequest;
import com.example.JunitPlayGround.junit5playground.model.ApiResponse;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WiremockCustom
public class ApiControllerTestJunit5IT {

    private TestRestTemplate template = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    public void test_endpoint() throws URISyntaxException {
        URI uri = new URI(String.format("http://localhost:8087/ais/123/refresh/321/hello?id3=dsada", port));
        System.out.println(uri);
        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<Object> response = template.exchange(request, new ParameterizedTypeReference<Object>() {
        });

        System.out.println(response);
    }

    @Test
    public void test_endpoint2() throws URISyntaxException {
        URI uri = new URI(String.format("http://localhost:8087/ais/123/refresh/321/hello", port));
        System.out.println(uri);
        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<Object> response = template.exchange(request, new ParameterizedTypeReference<Object>() {
        });

        System.out.println(response);
    }

    @Test
    public void test_endpoint3() throws URISyntaxException {
        URI uri = new URI(String.format("http://localhost:8087/ais/12321312321/create", port));
        System.out.println(uri);
        ApiRequest requestBody = new ApiRequest(5435, "message123");
        RequestEntity<?> request = new RequestEntity<>(requestBody, HttpMethod.POST, uri);
        ResponseEntity<Object> response = template.exchange(request, new ParameterizedTypeReference<>() {
        });

        System.out.println(response);
    }
}
