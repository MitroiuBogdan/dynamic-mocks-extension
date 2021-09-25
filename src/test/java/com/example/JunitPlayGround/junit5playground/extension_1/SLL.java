package com.example.JunitPlayGround.junit5playground.extension_1;


import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@ExtendWith(SSLExtension.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SLL {

    String value();
}
