package com.yahya.interpreter.ash.exceptions;

public class ReturnException extends RuntimeException {
    public final Object value;

    public ReturnException(Object value) {
        super(null, null, false, false);
        this.value = value;
    }
}
