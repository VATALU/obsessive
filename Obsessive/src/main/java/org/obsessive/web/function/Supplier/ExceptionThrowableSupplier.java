package org.obsessive.web.function.Supplier;

/**
 * This functional interface coudl throw out java.lang.Exception
 */
@FunctionalInterface
public interface ExceptionThrowableSupplier<T> {
    T get() throws Exception;
}