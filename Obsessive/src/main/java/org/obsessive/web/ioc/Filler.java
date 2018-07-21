package org.obsessive.web.ioc;

import io.vertx.ext.web.RoutingContext;

public interface Filler {


    Object apply(final String name, final Class<?> paramType, final RoutingContext routingContext);
}
