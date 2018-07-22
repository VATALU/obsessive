package org.obsessive.web.lang.annotation.bind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface SessionAttribute {
    String value() default "";

    boolean required() default true;
}