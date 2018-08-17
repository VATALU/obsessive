package org.obsessive.web.ioc;

import org.obsessive.web.entity.config.ObsessiveOptions;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface Storage {

    //Beans
    ConcurrentMap<String, Object> SINGLETON_BEANS = new ConcurrentHashMap<>();
    //Classes
    ConcurrentMap<String, Class<?>> CLASSES = new ConcurrentHashMap<>();

}