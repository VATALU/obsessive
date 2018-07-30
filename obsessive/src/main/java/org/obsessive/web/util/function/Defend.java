package org.obsessive.web.util.function;

import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.executor.JvmExecutor;
import org.obsessive.web.util.function.supplier.JvmSupplier;

import java.util.Optional;

class Defend {

    private final static Record LOGGER = Record.get(Defend.class);

    static void safeExec(final JvmExecutor jvmExecutor, final Record record) {
        try {
            jvmExecutor.execute();
        } catch (final Throwable throwable) {
            record.jvm(throwable);
        }
    }

    static <T> T safeGet(final JvmSupplier<T> jvmSupplier, final Record record) {
        T t = null;
        try {
            t = jvmSupplier.get();
        } catch (final Throwable throwable) {
            record.jvm(throwable);
        }
        return t;
    }
}