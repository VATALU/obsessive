package org.obsessive.web.util.function.supplier;

import org.obsessive.web.lang.exception.ObsessiveException;


@FunctionalInterface
public interface ObsessiveSupplier<T> {
    T get() throws ObsessiveException;
}
