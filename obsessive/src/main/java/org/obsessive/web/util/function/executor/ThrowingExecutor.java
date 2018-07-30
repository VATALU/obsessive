package org.obsessive.web.util.function.executor;

@FunctionalInterface
public interface ThrowingExecutor {
    void execute() throws Exception;
}