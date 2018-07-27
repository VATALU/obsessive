package org.obsessive.web.util;

import org.obsessive.web.util.function.Fn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AnnotationUtil {

    /**
     * get the class's annotation map
     * @param clazz
     * @return
     */
    public static ConcurrentMap<String, Annotation> get(final Class<?> clazz) {
        return Fn.get(() -> {
            final Annotation[] annotationes = clazz.getDeclaredAnnotations();
            // Zapper
            return Fn.zipper(annotationes,
                    (item) -> item.annotationType().getName(),
                    (item) -> item);
        }, clazz);
    }

    /**
     * Check whether current field marked with specific annotation
     * @param field
     * @param annoCls
     * @return
     */
    public static boolean isFieldMark(final Field field, final Set<Class<? extends Annotation>> annoCls) {
        return annoCls.stream().anyMatch(field::isAnnotationPresent);
    }

    /**
     * Check whether class contains annotated field
     *
     * @param clazz
     * @param annoCls
     * @return
     */
    public static boolean isFieldMark(final Class<?> clazz, final Set<Class<? extends Annotation>> annoCls) {
        final List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        return fields.stream().anyMatch(field -> isFieldMark(field, annoCls));
    }

    /**
     * Check whether current class marked with specific annotation
     * @param clazz
     * @param annoCls
     * @return
     */
    public static boolean isClassMark(final Class<?> clazz, final Set<Class<? extends Annotation>> annoCls) {
        return annoCls.stream().anyMatch(clazz::isAnnotationPresent);
    }

    public static Set<Class<?>> getAnnotatedCls(final Set<Class<?>> classSet, final Set<Class<? extends Annotation>> annoClsSet) {
        return annoClsSet.stream().map(annoCls -> getAnnotatedCls(classSet, annoCls)).flatMap(Set::stream).collect(Collectors.toSet());
    }

    public static Set<Class<?>> getAnnotatedCls(final Set<Class<?>> classSet, final Class<? extends Annotation> annoCls) {
        return classSet.stream().filter(clazz -> clazz.isAnnotationPresent(annoCls)).collect(Collectors.toSet());
    }
}