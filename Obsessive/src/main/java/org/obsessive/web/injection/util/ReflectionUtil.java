package org.obsessive.web.injection.util;

import org.obsessive.web.injection.Storage;
import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.Func;
import org.obsessive.web.util.MapUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class ReflectionUtil {

    private static final Record RECORD = Record.get(ReflectionUtil.class);

    /**
     * create and return instance
     *
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
     *
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T instance(final Class<?> clazz, Object... params) {
        final Object o = Func.getJvm(() -> construct(clazz, params), clazz);
        return Func.getJvm(() -> (T) o, o);
    }

    /**
     * create and return instance
     * put instance in singleton map
     *
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
     *
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T singleton(final Class<?> clazz, final Object... params) {
        final Object o = MapUtil.increase(Storage.SINGLETON_BEANS, clazz.getName(), () -> instance(clazz, params));
        return Func.getJvm(() -> (T) o, o);
    }

    /**
     * getJvm class by className
     *
     * @param className
     * @return
     */
    public static Class<?> clazz(final String className) {
        return MapUtil.increase(Storage.CLASSES, className,
                () -> Func.getJvm(
                        () -> Thread.currentThread().getContextClassLoader().loadClass(className), className)
        );
    }

    /**
     * create and return instance
     *
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    private static <T> T construct(final Class<?> clazz, final Object... params) {
        return Func.getJvm(() -> {
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
                    t = Func.getJvm(() -> (T) o, o);
                }
            }
            return t;
        }, clazz, params);
    }

    /**
     * create and return instance
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> T construct(final Class<?> clazz) {
        final Object o = Func.getJvm(() -> clazz.newInstance(), clazz);
        return Func.getJvm(() -> (T) o, o);
    }

    /**
     * Whether is the clazz match the interfaceClass
     *
     * @param clazz
     * @param interfaceClass
     * @return
     */
    public static boolean isMatch(final Class<?> clazz, final Class<?> interfaceClass) {
        final Class<?>[] interfaces = clazz.getInterfaces();
        return Arrays.stream(interfaces).anyMatch(item -> Objects.equals(item, interfaceClass));
    }

    public static <T> void setField(final Object instance, final String fieldName, final T value) {
        Func.exec(() ->
                Func.safeExec(() -> {
                    final Field field = instance.getClass().getDeclaredField(fieldName);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.set(instance, value);
                }, RECORD), instance, fieldName);
    }

    public static <T> T invokeMethod(final Object instance, final String methodName, final Object... params) {
        return Func.get(() -> {
            Method method = null;
            try {
                //TODO distinguish different methods have the same name.
                method = instance.getClass().getMethod(methodName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            final Method m = method;
            return Func.get(() -> invokeMethod(instance, m, params), method);
        }, instance, params);
    }

    public static <T> T invokeMethod(final Object instance, final Method method, final Object... params) {
        return Func.get(() -> {
            method.setAccessible(true);
            Object result = null;
            try {
                result = method.invoke(instance, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            final Object o = result;
            return Func.get(() -> (T) o, o);
        }, instance, params);
    }

    // forbid to instance
    private ReflectionUtil() {
    }
}