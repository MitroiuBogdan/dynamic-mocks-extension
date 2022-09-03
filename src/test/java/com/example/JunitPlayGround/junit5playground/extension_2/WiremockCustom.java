package com.example.JunitPlayGround.junit5playground.extension_2;


import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(WiremockExtension.class)
public @interface WiremockCustom {
}
