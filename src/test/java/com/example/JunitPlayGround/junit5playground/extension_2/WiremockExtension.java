package com.example.JunitPlayGround.junit5playground.extension_2;

import com.example.JunitPlayGround.junit5playground.CustomBodyTransformer;
import com.example.JunitPlayGround.junit5playground.initializers.YlluxBodyTransformer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.opentable.extension.BodyTransformer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class WiremockExtension implements BeforeAllCallback, AfterAllCallback {

    private WireMockServer wireMockServer;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        wireMockServer = new WireMockServer(new WireMockConfiguration()
                .port(8087)
                .httpsPort(8088)
                .extensions(new BodyTransformer()));
        System.out.println("wiremockHasStarted");
        wireMockServer.start();

    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        wireMockServer.stop();
        System.out.println("wiremockHasStopped");
    }
}
