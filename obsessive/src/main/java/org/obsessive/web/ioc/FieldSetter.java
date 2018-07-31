package org.obsessive.web.ioc;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

public interface FieldSetter {
    void inject(ConcurrentMap<String, Object> singleton,Object bean,Field field);
}
