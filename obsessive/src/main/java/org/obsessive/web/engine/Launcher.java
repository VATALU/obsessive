package org.obsessive.web.engine;

import io.vertx.core.Vertx;

import java.util.function.Consumer;

public interface Launcher {
    void launch(Consumer<Vertx> consumer);
}
