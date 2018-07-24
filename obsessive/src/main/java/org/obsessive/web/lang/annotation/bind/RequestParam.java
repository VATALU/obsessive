package org.obsessive.web.lang.annotation.bind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestParam {
    String value() default "";

    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
