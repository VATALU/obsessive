package org.obsessive.web.util.function.supplier;

/**
 * This functional interface could throw out java.lang.Exception
 */
@FunctionalInterface
public interface JvmSupplier<T> {
    T get() throws Exception;
}