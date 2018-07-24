package org.obsessive.web.lang.annotation.bind;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestBody {

    boolean required() default true;

}
