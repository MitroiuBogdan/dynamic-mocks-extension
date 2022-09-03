package com.example.JunitPlayGround.junit5playground.extension_2;

import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

public class FieldExtension implements BeforeEachCallback {

//    @Override
//    public void afterEach(ExtensionContext extensionContext) throws Exception {
//        System.out.println("END ---");
//    }

//    @Override
//    public void beforeAll(ExtensionContext extensionContext) throws Exception {
//        System.out.println(extensionContext.getDisplayName() + " Has Started");
//        extensionContext.getElement()
//                .ifPresent(annotatedElement -> AnnotationSupport.findAnnotatedFieldValues(annotatedElement, SSLTemplate.class)
//                        .stream()
//                        .filter(o -> isInstanceof(o))
//                        .forEach(o -> {
//
//                                    o = new TestRestTemplate();
//                                    System.out.println("START");
//                                }
//                        ));
//
//  }

    public static boolean isInstanceof(Object o) {
        if (o instanceof TestRestTemplate) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println(extensionContext.getDisplayName() + " Has Started");

        List<Object> testInstances =
                extensionContext.getRequiredTestInstances().getAllInstances();

        testInstances.forEach(o -> System.out.println(o.getClass()));

        System.out.println(testInstances);
        System.out.println(extensionContext.getDisplayName() + " Has Started");
        extensionContext.getElement()
                .ifPresent(annotatedElement -> AnnotationSupport.findAnnotation(annotatedElement, SSLTemplate.class)
                        .stream().forEach(sll -> System.out.println(sll.restTemplate())));

//        extensionContext


    }
}
