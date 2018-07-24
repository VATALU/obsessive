package org.obsessive.web.log;

import io.vertx.core.VertxException;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.obsessive.web.lang.exception.ObsessiveException;

//TODO
public class CommonRecord implements Record {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(CommonRecord.class);

    public CommonRecord(final Class<?> clazz) {
    }

    @Override
    public void vertx(VertxException e) {

    }

    @Override
    public void jvm(Throwable throwable) {

    }

    @Override
    public void obsessive(ObsessiveException obsessiveException) {

    }

    @Override
    public void warn(String key, Object... args) {

    }

    @Override
    public void info(String key, Object... args) {

    }

    @Override
    public void debug(String key, Object... args) {

    }

    @Override
    public void error(String key, Object... args) {

    }
}