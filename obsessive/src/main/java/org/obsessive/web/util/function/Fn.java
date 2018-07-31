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

/**
 * wrapper of other functional utils
 *
 */
public class Fn {

    private static final Record LOGGER = Record.get(Fn.class);

    // Defend wrapper

    /**
     * only when params are all not null,it will return not null
     * if supplier.get is null,it will return default value
     *
     * @param defaultVaule
     * @param record
     * @param jvmSupplier
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T getJvm(final T defaultVaule,
                               final Record record,
                               final JvmSupplier<T> jvmSupplier,
                               final Object... params) {
        //supplier mustn't null
        Objects.requireNonNull(jvmSupplier);
        return Defend.getJvm(defaultVaule, Optional.ofNullable(record).orElse(LOGGER), jvmSupplier, params);
    }

    public static <T> T getJvm(final Record record,
                               final JvmSupplier<T> jvmSupplier,
                               final Object... params) {
        return getJvm(null, record, jvmSupplier, params);
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

    /**
     * @param jvmSupplier
     * @param record
     * @param <T>
     * @return
     */
    public static <T> T safeGet(final JvmSupplier<T> jvmSupplier, final Record record) {
        return Defend.safeGet(jvmSupplier, record);
    }

    //Obsessive wrapper

    /**
     * if parmas arn't all not null,it will return default value
     * then if supplier.get is null,it will return default value.
     *
     * @param supplier
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T get(final T defaultValue, final Supplier<T> supplier, final Object... params) {
        Objects.requireNonNull(supplier);
        return Obsessive.get(defaultValue, supplier, params);
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
     * execute function only when params aren't null
     *
     * @param executor
     * @param params
     */
    public static void exec(final Executor executor, final Object... params) {
        Objects.requireNonNull(executor);
        Obsessive.exec(executor,params);
    }


}