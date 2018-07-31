package org.obsessive.web.ioc.field;

import org.obsessive.web.ioc.FieldSetter;
import org.obsessive.web.util.Reflections;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

public class InjectSetter implements FieldSetter{
    @Override
    public void inject(ConcurrentMap<String, Object> singletonBeans, Object bean, Field field) {
        String fieldName = field.getName();
        Object instance = singletonBeans.get(fieldName);
        Reflections.setField(bean, field, instance);
    }
}