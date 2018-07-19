package org.obsessive.web.util;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class MapUtil {

    public static <K, V> V increase(final ConcurrentMap<K, V> kvConcurrentMap, final K key, final Supplier<V> vSupplier) {
        V v = kvConcurrentMap.get(key);
        if (null == v) {
            v = vSupplier.get();
            if (null != v) {
                kvConcurrentMap.put(key, v);
            }
        }
        return v;
    }
}