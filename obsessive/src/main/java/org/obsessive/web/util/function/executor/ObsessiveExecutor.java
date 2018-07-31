package org.obsessive.web.util.function.executor;

import org.obsessive.web.lang.exception.ObsessiveException;

@FunctionalInterface
public interface ObsessiveExecutor {
    void execute() throws ObsessiveException;
}
