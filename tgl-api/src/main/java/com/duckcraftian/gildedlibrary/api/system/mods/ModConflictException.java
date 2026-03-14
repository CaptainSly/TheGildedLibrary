package com.duckcraftian.gildedlibrary.api.system.mods;

public class ModConflictException extends RuntimeException {

    public ModConflictException() {
        super();
    }

    public ModConflictException(String message) {
        super(message);
    }

    public ModConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModConflictException(Throwable cause) {
        super(cause);
    }

    protected ModConflictException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
