package org.obsessive.web.entity.constant;

import org.obsessive.web.ioc.FieldSetter;
import org.obsessive.web.ioc.field.InjectSetter;
import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.lang.annotation.Value;
import org.obsessive.web.util.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

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

    Map<Class<? extends Annotation>, FieldSetter> FIELD_SETTER_MAP = new HashMap<Class<? extends Annotation>, FieldSetter>(){{
        this.put(Inject.class, Reflections.instance(Inject.class));
        this.put(Value.class,Reflections.instance(Value.class));
    }};

}