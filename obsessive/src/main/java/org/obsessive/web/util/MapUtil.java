package org.obsessive.web.util;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapUtil {

    /**
     * if map doesn't contain the value, put the supplier getJvm value in this key.
     *
     * @param kvConcurrentMap
     * @param key
     * @param vSupplier
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V increase(final ConcurrentMap<K, V> kvConcurrentMap, final K key, final Supplier<V> vSupplier) {
        V v = kvConcurrentMap.get(key);
        if (v == null) {
            v = vSupplier.get();
            if (v != null) {
                kvConcurrentMap.put(key, v);
            }
        }
        return v;
    }

    /**
     * zipper
     * @param object
     * @param keyFunc
     * @param valueFunc
     * @param <K>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <K, V, E> ConcurrentMap<K, V> zipper(final Collection<E> object, final Function<E, K> keyFunc, final Function<E, V> valueFunc) {
        final ConcurrentMap<K, V> kvConcurrentHashMap = new ConcurrentHashMap<>();
        if (object.size() > 0) {
            for (final E item : object) {
                if (null != item) {
                    final K key = keyFunc.apply(item);
                    final V value = valueFunc.apply(item);
                    if (key != null && value != null) {
                        kvConcurrentHashMap.put(key, value);
                    }
                }
            }
        }
        return kvConcurrentHashMap;
    }
}