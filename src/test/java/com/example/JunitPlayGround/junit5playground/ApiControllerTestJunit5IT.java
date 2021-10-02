package com.example.JunitPlayGround.junit5playground;

import com.example.JunitPlayGround.junit5playground.extension_2.WiremockCustom;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach

    @Test
    public void test_endpoint() throws URISyntaxException {
        URI uri = new URI(String.format("http://localhost:8087/ais/refresh", port));
        System.out.println(uri);
        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<Object> response = template.exchange(request, new ParameterizedTypeReference<Object>() {
        });

        System.out.println(response);
    }

    @Test
    public void test_endpoint2() throws URISyntaxException {
        URI uri = new URI(String.format("http://localhost:8087/ais/refresh", port));
        System.out.println(uri);
        RequestEntity<Void> request = new RequestEntity<>(HttpMethod.GET, uri);
        ResponseEntity<Object> response = template.exchange(request, new ParameterizedTypeReference<Object>() {
        });

        System.out.println(response);
    }
}
