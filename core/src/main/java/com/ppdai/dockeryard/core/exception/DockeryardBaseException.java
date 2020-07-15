package com.ppdai.dockeryard.core.exception;


public class DockeryardBaseException extends RuntimeException {

    public DockeryardBaseException(String message) {
        super(message);
    }

    public DockeryardBaseException(String message, Throwable e) {
        super(message, e);
    }

    public DockeryardBaseException(Throwable e) {
        super(e);
    }
}
