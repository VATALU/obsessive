package org.obsessive.web.util;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class MapUtil {

    /** if map doesn't contain the value, put the supplier getJvm value in this key.
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
}