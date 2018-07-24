package org.obsessive.web.lang.annotation;

import io.vertx.core.http.HttpMethod;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Route {

    String value();

    HttpMethod[] method() default {HttpMethod.GET};

    String[] consumes() default {};

    String[] produces() default {};
}
