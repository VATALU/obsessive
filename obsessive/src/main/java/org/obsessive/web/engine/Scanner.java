package org.obsessive.web.engine;

import org.obsessive.web.entity.constant.AnnotationConstant;
import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.lang.annotation.Value;
import org.obsessive.web.log.Record;
import org.obsessive.web.util.Annotations;
import org.obsessive.web.util.ClassUtil;
import org.obsessive.web.util.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scanner {

    private final static Record LOGGER  = Record.get(Scanner.class);

    //scan class under this package
    public static ConcurrentMap<String, Class<?>> scan (final Class<?> clazz) {
        //get all class
        Set<Class<?>> classSet = ClassUtil.getClassSet(clazz.getPackage().getName());
        //filter annotationed class
        Set<Class<?>> annotatedClsSet = Annotations.getAnnotatedCls(classSet, AnnotationConstant.MODULE);
        return annotatedClsSet.stream().collect(Collectors.toConcurrentMap(Class::getName,annotatedCls->annotatedCls));
    }

    public static ConcurrentMap<String, Object> inject(final ConcurrentMap<String, Class<?>> annotatedClsMap) {

        final ConcurrentMap<String, Object> singletonBeans = new ConcurrentHashMap<>();

        annotatedClsMap.forEach((className,clazz)->{
            //TODO null detection
            Object bean = Reflections.instance(clazz);
            singletonBeans.put(className,bean);
        });
        singletonBeans.forEach((className, bean)->{
            Field[] fields = bean.getClass().getFields();
            Stream<Field> fieldStream = Arrays.stream(fields);
            // @Inject
            fieldStream.filter(field -> field.isAnnotationPresent(Inject.class)).forEach(field -> {
                String fieldName = field.getName();
                Object instance = singletonBeans.get(fieldName);
                Reflections.setField(bean,field,instance);
            });
            // @Value
            fieldStream.filter(field -> field.isAnnotationPresent(Value.class)).forEach(field ->
                Reflections.setField(bean,field,field.getAnnotation(Value.class).value()));
        });
        return singletonBeans;
    }
}