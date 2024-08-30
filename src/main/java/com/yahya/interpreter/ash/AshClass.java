package com.yahya.interpreter.ash;

import java.util.List;
import java.util.Map;

public class AshClass implements AshCallable {

    public final String name;
    private final Map<String, AshFunction> methods;
    private final AshClass superclass;

    public AshClass(String name, AshClass superclass, Map<String, AshFunction> methods) {
        this.superclass = superclass;
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        AshFunction initializer = findInitMethod();
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        AshInstance instance = new AshInstance(this);
        AshFunction initializer = findInitMethod();
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    public AshFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        if (superclass != null) {
            return superclass.findMethod(name);
        }
        return null;
    }

    public AshFunction findInitMethod() {
        return findMethod("init");
    }
}
