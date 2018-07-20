package org.obsessive.web.factory;

import org.obsessive.web.lang.annotation.Inject;
import org.obsessive.web.lang.annotation.Value;
import org.obsessive.web.util.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class BeanFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanFactory.class);

    /**
     * 注入类与注入实例的映射
     */
    private final Map<Class<?>, Object> BEANS_MAP = new HashMap<>();

    public BeanFactory(Set<Class<?>> beanClassSet) {
        //创建类的实例
        beanClassSet.forEach(beanClass -> {
            Object obj = ReflectUtil.instance(beanClass);
            BEANS_MAP.put(beanClass, obj);
        });

        //获取所有的注入类与注入实例之间的映射关系
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
                                ReflectUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                        // @Value 的 Field
                        if (beanField.isAnnotationPresent(Value.class)) {
                            ReflectUtil.setField(beanInstance, beanField, beanField.getAnnotation(Value.class).value());
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
            throw new RuntimeException("can not getJvm " + clazz.getName());
        }
        return (T) BEANS_MAP.get(clazz);
    }

    /**
     * 获取对应类集合的Map实例
     */
    public Map<Class<?>, Object> getBeans(Set<Class<?>> classSet) {
        Map<Class<?>, Object> classObjectMap = new HashMap<>();
        classSet.forEach(clazz -> {
            if(BEANS_MAP.containsKey(clazz)) {
                classObjectMap.put(clazz,BEANS_MAP.get(clazz));
            } else {
                LOGGER.info("class[{}] is not found",clazz.getName());
            }
        });
        return classObjectMap;
    }

}
