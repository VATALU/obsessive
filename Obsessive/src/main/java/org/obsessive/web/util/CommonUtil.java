package org.obsessive.web.util;

import org.obsessive.web.function.Supplier.ExceptionThrowableSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    public static <T> T getValue(final ExceptionThrowableSupplier<T> supplier, final Object... params) {
        return getValue(null, supplier, params);
    }

    /**
     * only params are all not null will return not null
     * if supplier.get is null return default value
     *
     * @param defaultValue
     * @param supplier
     * @param params
     * @param <T>
     * @return T
     */
    public static <T> T getValue(final T defaultValue, final ExceptionThrowableSupplier<T> supplier, final Object... params) {
        T t = null;
        final boolean match = Arrays.stream(params).allMatch(CommonUtil::not);
        try {
            if (match) {
                t = supplier.get();
                if (t == null) {
                    t = defaultValue;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
        return t;
    }

    private static boolean is(final Object item) {
        return null == item;
    }

    private static boolean not(final Object item) {
        return !is(item);
    }
}