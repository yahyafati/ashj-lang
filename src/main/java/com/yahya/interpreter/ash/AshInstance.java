package com.yahya.interpreter.ash;

public class AshInstance {

    private final AshClass klass;

    public AshInstance(AshClass klass) {
        this.klass = klass;
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
