package org.obsessive.web.util;

import org.obsessive.web.entity.config.ObsessiveOptions;
import org.yaml.snakeyaml.Yaml;

public final class Yamls {

    private Yamls() {
    }

    public static <T> T loadAs(final String fileName,
                               final Class<T> clazz) {
        return new Yaml().loadAs(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName),
                clazz);
    }
}