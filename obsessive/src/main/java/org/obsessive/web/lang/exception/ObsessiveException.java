package org.obsessive.web.lang.exception;

public abstract class ObsessiveException extends Exception {
    private final String message;

    protected ObsessiveException(final String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public abstract int getCode();
}