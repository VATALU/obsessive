package org.obsessive.web.entity.config;

import io.vertx.core.VertxOptions;
import lombok.Data;

@Data
public class ObsessiveOptions {
    private VertxOptions vertxOptions;
}