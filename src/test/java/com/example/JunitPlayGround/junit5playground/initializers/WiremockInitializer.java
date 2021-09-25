package com.example.JunitPlayGround.junit5playground.initializers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().port(8087));

        wireMockServer.start();

        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextClosedEvent) {
                wireMockServer.stop();
            }
        });
        applicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);
    }
}
