package org.obsessive.web.util.function;

import org.obsessive.web.log.Record;
import org.obsessive.web.util.Maps;
import org.obsessive.web.util.function.executor.JvmExecutor;
import org.obsessive.web.util.function.executor.Executor;
import org.obsessive.web.util.function.supplier.JvmSupplier;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class Fn {

    private static final Record RECORD = Record.get(Fn.class);

    public static <T> T getJvm(final JvmSupplier<T> supplier, final Object... params) {
        return getJvm(null, supplier, params);
    }

    /**
     * only when params are all not null,it will return not null
     * if supplier.get is null,it will return default value
     *
     * @param defaultValue
     * @param supplier
     * @param params
     * @param <T>
     * @return T
     */
    public static <T> T getJvm(final T defaultValue, final JvmSupplier<T> supplier, final Object... params) {
        // supplier mustn't null
        Objects.requireNonNull(supplier);
        T t = null;
        final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
        // why not call Defend.safeGet ?
        try {
            if (match) {
                // use Optional
                t = Optional.ofNullable(supplier.get()).orElse(defaultValue);
            }
        } catch (Exception e) {
            RECORD.error(e.toString());
        }
        return t;
    }

    /**
     * only when params are all not null and supplier.get is not null, it will return default value
     * in other situation, it will return null.
     *
     * @param supplier
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T get(final Supplier<T> supplier, final Object... params) {
        return get(null, supplier, params);
    }

    /**
     * if parmas arn't all null,it will return default value
     * then if supplier.get is null,it will return default value.
     *
     * @param supplier
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T get(final T defaultValue, final Supplier<T> supplier, final Object... params) {
        final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
        if (match) {
            final T ret = supplier.get();
            return (ret == null) ? defaultValue : ret;
        } else {
            return defaultValue;
        }
    }

    /**
     * execute function only when params aren't null
     *
     * @param executor
     * @param params
     */
    public static void exec(final Executor executor, final Object... params) {
        if (params.length > 0) {
            final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
            if (match) {
                executor.execute();
            }
        } else {
            executor.execute();
        }
    }

    /**
     * execute function throws throwable
     *
     * @param jvmExecutor
     * @param record
     */
    public static void safeExec(final JvmExecutor jvmExecutor, final Record record) {
        Defend.safeExec(jvmExecutor, record);
    }

    public static <K, V, E> ConcurrentMap<K, V> zipper(final E[] object, final Function<E, K> keyFunc, final Function<E, V> valueFunc) {
        return Maps.zipper(Arrays.asList(object),keyFunc,valueFunc);
    }

    public static boolean is(final Object item) {
        return item == null;
    }

    public static boolean not(final Object item) {
        return !is(item);
    }
}