package org.obsessive.web.util;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.obsessive.web.ioc.Storage;
import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.Fn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class Reflections {

    private static final Record LOGGER = Record.get(Reflections.class);

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
        final Object o = Fn.getJvm(LOGGER,() -> construct(clazz, params), clazz);
        return Fn.getJvm(LOGGER, () -> (T) o, o);
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
        final Object o = Maps.increase(Storage.SINGLETON_BEANS, clazz.getName(), () -> instance(clazz, params));
        return Fn.getJvm(LOGGER, () -> (T) o, o);
    }

    /**
     * getJvm class by className
     *
     * @param className
     * @return
     */
    public static Class<?> clazz(final String className) {
        return Maps.increase(Storage.CLASSES, className,
                () -> Fn.getJvm(
                        LOGGER,
                        () -> Clazz.getClassLoader().loadClass(className),
                        className)
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
        return Fn.getJvm(LOGGER, () -> {
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
                    t = Fn.getJvm(LOGGER,() -> (T) o, o);
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
        final Object o = Fn.getJvm(LOGGER, () -> ConstructorAccess.get(clazz).newInstance(), clazz);
        return Fn.getJvm(LOGGER,() -> (T) o, o);
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
        Fn.exec(() ->
                Fn.safeExec(() -> {
                    FieldAccess fieldAccess = FieldAccess.get(instance.getClass());
                    fieldAccess.set(instance,fieldName,value);
                }, LOGGER), instance, fieldName);
    }

    public static <T> void setField(final Object instance, final Field field, final T value) {
        Fn.exec(() ->
                Fn.safeExec(() -> {
                    setField(instance,field.getName(),value);
                }, LOGGER), field, value);

    }

    public static <T> T invokeMethod(final Object instance, final String methodName, final Object... params) {
        return Fn.get(() -> {
            final MethodAccess access = MethodAccess.get(instance.getClass());
            Object result = null;
            result = access.invoke(instance, methodName, params);

//            Method method = null;
//            try {
//                //TODO distinguish different methods have the same name.
//                method = instance.getClass().getMethod(methodName);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }

            final Object o = result;
            return Fn.get(() -> (T) o, o);
        }, instance, params);
    }

    public static <T> T invokeMethod(final Object instance, final Method method, final Object... params) {
        return Fn.get(() -> {
            final String methodName = method.getName();
            return Fn.get(() -> invokeMethod(instance, methodName, params), params, methodName);
        }, instance, params);
    }

    // forbid to be instanced
    private Reflections() {
    }
}