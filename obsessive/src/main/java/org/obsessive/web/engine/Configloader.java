package org.obsessive.web.engine;

import io.vertx.core.VertxOptions;
import org.obsessive.web.entity.config.ObsessiveOptions;
import org.obsessive.web.entity.constant.ConfigConstant;
import org.obsessive.web.util.Yamls;

public class Configloader {

    public static ObsessiveOptions loadConfig() {
        return Yamls.loadAs(ConfigConstant.CONFIG_FILE, ObsessiveOptions.class);
    }
}