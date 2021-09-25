package com.example.JunitPlayGround.junit5playground.extension_2;

import com.example.JunitPlayGround.junit5playground.extension_1.SLL;
import com.example.JunitPlayGround.junit5playground.initializers.WiremockInitializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@WiremockCustom
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EnrichTestTemplateJunit5IT {


//    @SSLTemplate

    public TestRestTemplate template=new TestRestTemplate();

//    @SSLTemplate
    public String sslStrong="ana";

//    @Autowired
//    public WireMockServer wireMockServer;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void initSSL(){}

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
