package org.obsessive.web.lang.exception;

import io.vertx.core.VertxException;

public class ObsessiveRunException extends VertxException {
    public ObsessiveRunException(final String message) {
        super(message);
    }

    public ObsessiveRunException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ObsessiveRunException(final Throwable cause) {
        super(cause);
    }
}