package org.obsessive.web.lang.annotation.bind;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {
    String value() default "";

    boolean required() default true;

}
