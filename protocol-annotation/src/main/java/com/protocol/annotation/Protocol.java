package com.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by lianyagang on 2022年12月28日17:09:04
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface Protocol {

    String value();
}
