package org.obsessive.web.util.function;

import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.executor.JvmExecutor;
import org.obsessive.web.util.function.supplier.JvmSupplier;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

class Defend {

    private final static Record LOGGER = Record.get(Defend.class);

    /**
     *
     * @param jvmExecutor
     * @param record
     */
    static void safeExec(final JvmExecutor jvmExecutor, final Record record) {
        try {
            jvmExecutor.execute();
        } catch (final Throwable throwable) {
            Optional.ofNullable(record).orElse(LOGGER).jvm(throwable);
        }
    }

    /**
     *
     * @param jvmSupplier
     * @param record
     * @param <T>
     * @return
     */
    static <T> T safeGet(final JvmSupplier<T> jvmSupplier, final Record record) {
        T t = null;
        try {
            t = jvmSupplier.get();
        } catch (final Throwable throwable) {
            Optional.ofNullable(record).orElse(LOGGER).jvm(throwable);
        }
        return t;
    }

    /**
     *
     * @param defaultValue
     * @param record
     * @param supplier
     * @param params
     * @param <T>
     * @return
     */
    static <T> T getJvm(final T defaultValue,final Record record, final JvmSupplier<T> supplier, final Object... params) {
        if(params.length>0) {
            final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
            T t = null;
            if(match) {
                t = Optional.ofNullable(safeGet(supplier,record)).orElse(defaultValue);
            }
            return t;
        } else {
            return Optional.ofNullable(safeGet(supplier,record)).orElse(defaultValue);
        }

    }
}