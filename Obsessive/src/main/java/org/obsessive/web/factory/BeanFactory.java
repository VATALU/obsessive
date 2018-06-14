package org.obsessive.web.factory;

import org.obsessive.web.lang.annotation.Inject;
import org.obsessive.web.lang.annotation.Value;
import org.obsessive.web.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanFactory {

    /**
     * 注入类与注入实例的映射
     */
    private final Map<Class<?>, Object> BEANS_MAP = new HashMap<>();


    public BeanFactory(ClassFactory classFactory) {
        //获取所有注入的类
        Set<Class<?>> beanClassSet = classFactory.getAnnotationClassSet();
        //创建类的实例
        for (Class<?> beanClass : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(beanClass);
            BEANS_MAP.put(beanClass, obj);
        }

        /**
         * 获取所有的注入类与注入实例之间的映射关系
         */
        if (!BEANS_MAP.isEmpty()) {
            // 遍历 BEANS_MAP
            for (Map.Entry<Class<?>, Object> beanEntry : BEANS_MAP.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (beanFields != null && beanFields.length != 0) {
                    for (Field beanField : beanFields) {
                        // @Inject 的 Field
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 在 BEANS_MAP 中根据 Type 获取 BeanField 对应的实例
                            Class<?> beanFiledClass = beanField.getType();
                            Object beanFieldInstance = BEANS_MAP.get(beanFiledClass);
                            if (beanFieldInstance != null) {
                                // 通过反射初始化BeanField的值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                        // @Value 的 Field
                        if(beanField.isAnnotationPresent(Value.class)) {
                            ReflectionUtil.setField(beanInstance, beanField, beanField.getAnnotation(Value.class).value());
                        }
                    }
                }
            }
        }
    }


    /**
     * 获取 Bean 映射
     *
     * @return
     */
    public Map<Class<?>, Object> getBeansMap() {
        return BEANS_MAP;
    }

    /**
     * 获取 Bean 实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        if (!BEANS_MAP.containsKey(clazz)) {
            throw new RuntimeException("can not get "+clazz.getName());
        }
        return (T) BEANS_MAP.get(clazz);
    }


}
