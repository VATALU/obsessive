package org.obsessive.web.util.function.executor;

@FunctionalInterface
public interface ExceptionExecutor {
    void execute() throws Exception;
}