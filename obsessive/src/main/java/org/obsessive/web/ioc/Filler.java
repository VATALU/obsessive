package org.obsessive.web.ioc;

import io.vertx.ext.web.RoutingContext;

public interface Filler {

    /**
     *
     * @param name
     * @param paramType
     * @param routingContext
     * @return
     */
    Object apply(final String name, final Class<?> paramType, final RoutingContext routingContext);
}
