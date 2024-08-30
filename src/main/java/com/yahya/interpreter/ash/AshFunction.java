package com.yahya.interpreter.ash;

import com.yahya.interpreter.ash.exceptions.ReturnException;
import com.yahya.interpreter.ash.stmt.Function;

import java.util.List;

public class AshFunction implements AshCallable {
    private final Function declaration;

    private final Environment closure;
    private final boolean isInitializer;

    public AshFunction(Function declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (ReturnException returnValue) {
            if (isInitializer) {
                return closure.getAt(0, "this");
            }
            return returnValue.value;
        }

        return null;
    }

    AshFunction bind(AshInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new AshFunction(declaration, environment, isInitializer);
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
