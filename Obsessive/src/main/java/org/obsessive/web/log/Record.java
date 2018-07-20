package org.obsessive.web.log;

import io.vertx.core.VertxException;
import org.obsessive.web.lang.exception.ObsessiveException;

/**
 * combine vertx logging system with obsessive's
 */
public interface Record {
    void vertx(VertxException e);

    void jvm(Throwable throwable);

    void obsessive(ObsessiveException obsessiveException);

    void warn(String key, Object... args);

    void info(String key, Object... args);

    void debug(String key, Object... args);

    void error(String key, Object... args);

    static Record get(final Class<?> clazz) {return new CommonRecord(clazz);}
}
