package com.example.JunitPlayGround.junit5playground.extension_1;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public class SSLExtension implements AfterAllCallback, BeforeAllCallback {

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        System.out.println(extensionContext.getDisplayName() + " Has Ended");
        String value = getValueId(extensionContext);
        if (value != null) {
            System.out.println(value);
        }
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        System.out.println(extensionContext.getDisplayName() + " Has Started");
        extensionContext.getElement()
                .ifPresent(annotatedElement -> AnnotationSupport.findAnnotation(annotatedElement, SLL.class)
                        .map(annotation -> startProcessing(extensionContext, annotation.value()))
                        .ifPresent(valueId -> store(extensionContext).put("valueId", valueId)));

    }


    private String startProcessing(ExtensionContext extensionContext, String original) {
        return original.toUpperCase();
    }

    public static String getValueId(ExtensionContext extensionContext) {
        return store(extensionContext).get("valueId", String.class);
    }

    private static ExtensionContext.Store store(ExtensionContext extensionContext) {
        return extensionContext.getStore(ExtensionContext.Namespace.create(SSLExtension.class, extensionContext.getUniqueId()));

    }
}
