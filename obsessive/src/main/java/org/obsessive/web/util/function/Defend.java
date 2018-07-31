package org.obsessive.web.util.function;

import io.vertx.core.VertxException;
import org.obsessive.web.lang.exception.ObsessiveException;
import org.obsessive.web.lang.exception.ObsessiveRunException;
import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.executor.JvmExecutor;
import org.obsessive.web.util.function.executor.ObsessiveExecutor;
import org.obsessive.web.util.function.supplier.JvmSupplier;
import org.obsessive.web.util.function.supplier.ObsessiveSupplier;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * try catch exception util
 */
class Defend {

    private final static Record LOGGER = Record.get(Defend.class);

    /**
     * @param jvmExecutor
     * @param record
     */
    static void safeExec(final JvmExecutor jvmExecutor,
                         final Record record) {
        try {
            jvmExecutor.execute();
        } catch (final Throwable throwable) {
            Optional.ofNullable(record).orElse(LOGGER).jvm(throwable);
        }
    }

    /**
     * @param jvmSupplier
     * @param record
     * @param <T>
     * @return
     */
    static <T> T safeGet(final JvmSupplier<T> jvmSupplier,
                         final Record record) {
        T t = null;
        try {
            t = jvmSupplier.get();
        } catch (final Throwable throwable) {
            Optional.ofNullable(record).orElse(LOGGER).jvm(throwable);
        }
        return t;
    }

    /**
     * @param obsessiveSupplier
     * @param record
     * @param <T>
     * @return
     */
    static <T> T safeObseGet(final ObsessiveSupplier<T> obsessiveSupplier,
                             final Record record) {
        T t = null;
        try {
            t = obsessiveSupplier.get();
        } catch (final ObsessiveException e) {
            Optional.ofNullable(record).orElse(LOGGER).obsessive(e);
        } catch (final ObsessiveRunException e) {
            Optional.ofNullable(record).orElse(LOGGER).vertx(e);
            throw e;
        } catch (final VertxException e) {
            Optional.ofNullable(record).orElse(LOGGER).vertx(e);
        } catch (final Throwable e) {
            Optional.ofNullable(record).orElse(LOGGER).jvm(e);
        }
        return t;
    }

    /**
     *
     * @param obsessiveExecutor
     * @param record
     */
    static void safeObseExec(final ObsessiveExecutor obsessiveExecutor,
                             final Record record) {
        try {
            obsessiveExecutor.execute();
        } catch (final ObsessiveException e) {
            Optional.ofNullable(record).orElse(LOGGER).obsessive(e);
        } catch (final VertxException e) {
            Optional.ofNullable(record).orElse(LOGGER).vertx(e);
        } catch (final Throwable e) {
            Optional.ofNullable(record).orElse(LOGGER).jvm(e);
        }
    }

    /**
     * @param defaultValue
     * @param record
     * @param supplier
     * @param params
     * @param <T>
     * @return
     */
    static <T> T getJvm(final T defaultValue,
                        final Record record,
                        final JvmSupplier<T> supplier,
                        final Object... params) {
        if (params.length > 0) {
            final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
            T t = null;
            if (match) {
                t = Optional.ofNullable(safeGet(supplier, record)).orElse(defaultValue);
            }
            return t;
        } else {
            return Optional.ofNullable(safeGet(supplier, record)).orElse(defaultValue);
        }

    }


}