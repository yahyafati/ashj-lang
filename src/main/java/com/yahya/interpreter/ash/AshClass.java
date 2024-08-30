package com.yahya.interpreter.ash;

import java.util.List;

public class AshClass implements AshCallable {

    public final String name;

    public AshClass(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        AshInstance instance = new AshInstance(this);
        return instance;
    }
}
