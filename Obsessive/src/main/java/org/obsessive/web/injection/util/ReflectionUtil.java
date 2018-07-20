package org.obsessive.web.injection.util;

import org.obsessive.web.injection.Storage;
import org.obsessive.web.util.CommonUtil;
import org.obsessive.web.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class ReflectionUtil {

    /**
     * create and return instance
     * @param className
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T instance(final String className, Object... params) {
        return instance(clazz(className), params);
    }

    /**
     * create and return instance
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T instance(final Class<?> clazz, Object... params) {
        final Object o = CommonUtil.getValue(() -> construct(clazz, params), clazz);
        return CommonUtil.getValue(() -> (T) o, o);
    }

    /**
     * create and return instance
     * put instance in singleton map
     * @param className
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T singleton(final String className, final Object... params) {
        return singleton(clazz(className), params);
    }

    /**
     * create and return instance
     * put instance in singleton map
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T singleton(final Class<?> clazz, final Object... params) {
        final Object o = MapUtil.increase(Storage.SINGLETON_BEANS, clazz.getName(), () -> instance(clazz, params));
        return CommonUtil.getValue(() -> (T) o, o);
    }

    /**
     * get class by className
     * @param className
     * @return
     */
    public static Class<?> clazz(final String className) {
        return MapUtil.increase(Storage.CLASSES, className,
                () -> CommonUtil.getValue(
                        () -> Thread.currentThread().getContextClassLoader().loadClass(className), className)
        );
    }

    /**
     * create and return instance
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    private static <T> T construct(final Class<?> clazz, final Object... params) {
        return CommonUtil.getValue(() -> {
            T t = null;
            final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
            for (Constructor<?> constructor : constructors) {
                //if has not param
                if (params.length == 0) {
                    t = construct(clazz);
                }
                //compare argument length
                if (params.length == constructor.getParameterTypes().length) {
                    final Object o = constructor.newInstance(params);
                    t = CommonUtil.getValue(() -> (T) o, o);
                }
            }
            return t;
        }, clazz, params);
    }

    /**
     * create and return instance
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T construct(final Class<?> clazz) {
        final Object o = CommonUtil.getValue(() -> clazz.newInstance(), clazz);
        return CommonUtil.getValue(() -> (T) o, o);
    }

    /**
     *
     * @param clazz
     * @param interfaceClass
     * @return
     */
    public static boolean isMatch(final Class<?> clazz, final Class<?> interfaceClass) {
        final Class<?>[] interfaces = clazz.getInterfaces();
        return Arrays.stream(interfaces).anyMatch(item-> Objects.equals(item,interfaceClass));
    }



    // forbid to instance
    private ReflectionUtil() {
    }
}