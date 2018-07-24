package org.obsessive.web.lang.annotation.bind;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Value {
    String value();
}
