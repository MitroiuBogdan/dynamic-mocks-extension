package com.example.JunitPlayGround.junit5playground.extension_2;

import com.example.JunitPlayGround.junit5playground.extension_1.SSLEnabled;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.stream.Stream;

public class RestTemplatePostProcessorExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {

        Stream.of(testInstance.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SSLEnabled.class) && (field.getType() == TestRestTemplate.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    TestRestTemplate testRestTemplate = new TestRestTemplate();
                    try {
                        field.set(testInstance, testRestTemplate);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });

//        Stream.of(testInstance.getClass().getDeclaredFields())
//                .filter(field -> field.getType() == TestRestTemplate.class)
//                .forEach(field -> {
//                    field.setAccessible(true);
//                    TestRestTemplate testRestTemplate = new TestRestTemplate();
//                    try {
//                        field.set(testInstance, testRestTemplate);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                });
    }
}
