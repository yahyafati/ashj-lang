package com.yahya.interpreter.ash;

import java.util.List;
import java.util.Map;

public class AshClass implements AshCallable {

    public final String name;
    private final Map<String, AshFunction> methods;

    public AshClass(String name, Map<String, AshFunction> methods) {
        this.name = name;
        this.methods = methods;
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
        return new AshInstance(this);
    }

    public AshFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        return null;
    }
}
