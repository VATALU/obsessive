package org.obsessive.web.ioc.field;

import org.obsessive.web.ioc.FieldSetter;
import org.obsessive.web.lang.annotation.Value;
import org.obsessive.web.util.Reflections;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

public class ValueSetter implements FieldSetter {
    @Override
    public void inject(ConcurrentMap<String, Object> singleton, Object bean, Field field) {
        //TODO more type parser
        Reflections.setField(bean, field, field.getAnnotation(Value.class).value());
    }
}