package org.obsessive.tool;

import org.obsessive.util.annotation.REST;
import org.obsessive.util.annotation.Service;
import org.obsessive.util.ClassUtil;
import org.obsessive.util.ConfigUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类助手类
 */
public class ClassHelper {
    public static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigUtil.getAppBasePackage();
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

        for(Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
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
        for(Class<?> cls :CLASS_SET){
            if(cls.isAnnotationPresent(REST.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }
}
