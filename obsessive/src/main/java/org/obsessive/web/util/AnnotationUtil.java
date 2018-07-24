package org.obsessive.web.util;

import org.obsessive.web.util.function.Func;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public final class AnnotationUtil {

    /**
     * get the class's annotation map
     * @param clazz
     * @return
     */
    public static ConcurrentMap<String, Annotation> get(final Class<?> clazz) {
        return Func.get(() -> {
            final Annotation[] annotationes = clazz.getDeclaredAnnotations();
            // Zapper
            return Func.zipper(annotationes,
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
    public static boolean isMark(final Field field, final Set<Class<? extends Annotation>> annoCls) {
        return annoCls.stream().anyMatch(field::isAnnotationPresent);
    }

    /**
     * Check wether class contains annotated field
     *
     * @param clazz
     * @param annoCls
     * @return
     */
    public static boolean isMark(final Class<?> clazz, final Set<Class<? extends Annotation>> annoCls) {
        final List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        return fields.stream().anyMatch(field -> isMark(field, annoCls));
    }
}