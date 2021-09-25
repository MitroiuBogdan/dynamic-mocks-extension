package com.example.JunitPlayGround.junit5playground;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

//@RunWith(SpringRunner.class)
//@SpringBootTest(
//        properties = {"wiremock.dns=localhost:8070"},
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiControllerTestJunit4IT {

//    private TestRestTemplate template = new TestRestTemplate();
//
//    @Rule
//    public WireMockClassRule mockClassRule = new WireMockClassRule(WireMockConfiguration.wireMockConfig().port(8070));
//
//    @LocalServerPort
//    private int port;
//
//
//    @Test
//    public void test_endpoint() throws URISyntaxException {
//        URI uri=new URI(String.format("http://localhost:8070/ais/refresh",port));
//        System.out.println(uri);
//        RequestEntity<Void> request=new RequestEntity<>(HttpMethod.GET,uri);
//        ResponseEntity<String> response=template.exchange(request, new ParameterizedTypeReference<String>() {
//        }) ;
//
//        System.out.println(response);
//    }
}
