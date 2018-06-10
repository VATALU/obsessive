package org.obsessive.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 获取类的实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz) {
        Object obj;
        try {
            obj = clazz.newInstance();
        } catch (Exception e) {
            LOGGER.error("new instance class[{}] failure", clazz.getName(), e);
            throw new RuntimeException(e);
        }
        return obj;
    }


    /**
     * 设置类的属性值
     *
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            LOGGER.error("invoke method failure", e);
            throw new RuntimeException(e);
        }

    }
}
