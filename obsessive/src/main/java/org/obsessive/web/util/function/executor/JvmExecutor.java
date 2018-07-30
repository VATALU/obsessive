package org.obsessive.web.util.function.executor;

@FunctionalInterface
public interface JvmExecutor {
    void execute() throws Exception;
}