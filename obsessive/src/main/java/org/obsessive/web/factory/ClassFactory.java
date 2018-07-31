package org.obsessive.web.factory;

import org.obsessive.web.engine.Scanner;
import org.obsessive.web.lang.annotation.Component;
import org.obsessive.web.lang.annotation.Controller;
import org.obsessive.web.lang.annotation.Repository;
import org.obsessive.web.lang.annotation.Service;
import org.obsessive.web.util.Clazz;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 类助手类
 */
public class ClassFactory {

    //注解类的集合
    public final Set<Class<?>> CLASS_SET = new HashSet<>();

    //注解类类型的集合
    private final Set<Class<? extends Annotation>> annotationSet = new HashSet<>();

    public ClassFactory(Class<?> clazz) {
        annotationSet.addAll(Arrays.asList(Service.class, Controller.class, Repository.class, Component.class));
        //加载 clazz 目录同级别以及以下的类
        CLASS_SET.addAll(Scanner.getClassSet(clazz.getPackage().getName()));
    }

    /**
     * 获取包下所有的注入的类
     *
     * @return
     */
    public Set<Class<?>> getAnnotationClassSet() {
        Set<Class<?>> classSet = new HashSet<>();
        annotationSet.forEach(annotation -> {
            classSet.addAll(getAnnotationClassSet(annotation));
        });
        return classSet;
    }

    /**
     * 通过注解类来获取对应的被注解类的集合
     * @param annotation
     * @return
     */
    public Set<Class<?>> getAnnotationClassSet(Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = new HashSet<>();
        CLASS_SET.forEach(clazz->{
            if(clazz.isAnnotationPresent(annotation)) {
                classSet.add(clazz);
            }
        });
        return classSet;
    }
}
