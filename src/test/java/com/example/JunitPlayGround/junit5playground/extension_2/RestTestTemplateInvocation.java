package com.example.JunitPlayGround.junit5playground.extension_2;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.stream.Stream;

public class RestTestTemplateInvocation implements TestTemplateInvocationContextProvider {

    @Override
    public boolean supportsTestTemplate(ExtensionContext extensionContext) {
        return false;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
        return Stream.of();
    }

    private TestTemplateInvocationContext invokeSSlContext(TestRestTemplate testRestTemplate) {
        return new TestTemplateInvocationContext(){

        };
    }



}