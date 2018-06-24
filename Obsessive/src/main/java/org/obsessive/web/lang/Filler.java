package org.obsessive.web.lang;


import io.vertx.ext.web.RoutingContext;
import org.obsessive.web.lang.annotation.*;
import org.obsessive.web.lang.filler.*;
import org.obsessive.web.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface Filler {
    Object apply(String name, Class<?> clazz, RoutingContext routingContext);

    ConcurrentMap<Class<? extends Annotation>, Filler> PARAMS = new ConcurrentHashMap<Class<? extends Annotation>, Filler>() {
                {
                    this.put(QueryParam.class, ReflectionUtils.newInstance(QueryFiller.class));
                    this.put(FormParam.class, ReflectionUtils.newInstance(FormFiller.class));
                    this.put(PathParam.class, ReflectionUtils.newInstance(PathFiller.class));
                    this.put(HeaderParam.class, ReflectionUtils.newInstance(HeaderFiller.class));
                    this.put(CookieParam.class, ReflectionUtils.newInstance(CookieFiller.class));
                    this.put(BodyParam.class, ReflectionUtils.newInstance(EmptyFiller.class));
                    this.put(StreamParam.class, ReflectionUtils.newInstance(EmptyFiller.class));
                    this.put(SessionParam.class, ReflectionUtils.newInstance(SessionFiller.class));
                }
            };

    Set<Class<? extends Annotation>> NO_VALUE = new HashSet<Class<? extends Annotation>>() {
                {
                    this.add(BodyParam.class);
                    this.add(StreamParam.class);
                }
            };
}
