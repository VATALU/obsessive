package org.obsessive.web.factory;

import org.obsessive.web.lang.annotation.Immit;
import org.obsessive.web.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanFactory {

    /**
     * 注入类与注入实例的映射
     */
    private static final Map<Class<?>, Object> BEANS_MAP = new HashMap<Class<?>, Object>();

    static {
        /**
         * 获取所有的注入类与注入实例之间的映射关系
         */
        if (!BEANS_MAP.isEmpty()) {
            // 遍历BEANS_MAP
            for (Map.Entry<Class<?>, Object> beanEntry : BEANS_MAP.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (beanFields != null && beanFields.length != 0) {
                    for (Field beanField : beanFields) {
                        if (beanField.isAnnotationPresent(Immit.class)) {
                            // 在Beans中获取BeanField对应的实例
                            Class<?> beanFiledClass = beanField.getType();
                            Object beanFieldInstance = BEANS_MAP.get(beanFiledClass);
                            if (beanFieldInstance != null) {
                                // 通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }

    static {
        //获取所有注入的类
        Set<Class<?>> beanClassSet = ClassFactory.getAnnotationClassSet();
        //创建类的实例
        for (Class<?> beanClass : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEANS_MAP.put(beanClass, obj);
        }
    }

    /**
     * 获取 Bean 映射
     *
     * @return
     */
    public static Map<Class<?>, Object> getBeansMap() {
        return BEANS_MAP;
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (!BEANS_MAP.containsKey(clazz)) {
            throw new RuntimeException("can not get "+clazz.getName());
        }
        return (T) BEANS_MAP.get(clazz);
    }


}
