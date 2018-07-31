package org.obsessive.web.util.function;

import org.obsessive.web.log.Record;
import org.obsessive.web.util.function.executor.Executor;
import org.obsessive.web.util.function.supplier.JvmSupplier;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

class Obsessive {

    static <T> T get(final T defaultValue,
                     final Supplier<T> supplier,
                     final Object... params) {
        if (params.length > 0) {
            final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
            if (match) {
                return Optional.ofNullable(supplier.get()).orElse(defaultValue);
            } else {
                return defaultValue;
            }
        } else {
            return Optional.ofNullable(supplier.get()).orElse(defaultValue);
        }
    }

    static void exec(final Executor executor,
                     final Object... params) {
        if (params.length > 0) {
            final boolean match = Arrays.stream(params).allMatch(Objects::nonNull);
            if (match) {
                executor.execute();
            }
        } else {
            //quickly no need to check
            executor.execute();
        }
    }

}