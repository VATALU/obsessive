package org.obsessive.web.util.function.supplier;

/**
 * This functional interface coudl throw out java.lang.Exception
 */
@FunctionalInterface
public interface ExceptionSupplier<T> {
    T get() throws Exception;
}