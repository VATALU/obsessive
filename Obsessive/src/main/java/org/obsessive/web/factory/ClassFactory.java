package org.obsessive.web.factory;

import org.obsessive.web.lang.annotation.Controller;
import org.obsessive.web.lang.annotation.Service;
import org.obsessive.web.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类助手类
 */
public class ClassFactory {
    public static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigFactory.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取包下所有的注入的类
     * @return
     */
    public static Set<Class<?>> getAnnotationClassSet() {
        Set<Class<?>> annotationClassSet = new HashSet<Class<?>>();
        annotationClassSet.addAll(getServiceClassSet());
        annotationClassSet.addAll(getRESTClassSet());
        return annotationClassSet;
    }

    /**
     * 获取包下所有注入的 Service 类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();

        for(Class<?> clazz : CLASS_SET){
            if(clazz.isAnnotationPresent(Service.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * 获取包下所有注入的 REST 类
     * @return
     */
    public static Set<Class<?>> getRESTClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class<?> clazz :CLASS_SET){
            if(clazz.isAnnotationPresent(Controller.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }
}
