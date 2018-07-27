package org.obsessive.web.entity.constant;

import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.lang.annotation.Value;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public interface AnnotationConstant {
    // module annotations
     Set<Class<? extends Annotation>> MODULE = new HashSet<Class<? extends Annotation>>() {
        {
            this.add(Component.class);
            this.add(Controller.class);
            this.add(Repository.class);
            this.add(Service.class);
        }
    };

    // inject annotations
    Set<Class<? extends Annotation>> INJECT = new HashSet<Class<? extends Annotation>>() {
        {
            this.add(Inject.class);
            this.add(Value.class);
        }
    };


}