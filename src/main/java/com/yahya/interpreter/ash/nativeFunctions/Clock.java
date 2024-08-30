package com.yahya.interpreter.ash.nativeFunctions;

import com.yahya.interpreter.ash.AshCallable;
import com.yahya.interpreter.ash.Interpreter;

import java.util.List;

public class Clock implements AshCallable {
    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double) System.currentTimeMillis() / 1000.0;
    }

    @Override
    public String toString() {
        return "<native fn>";
    }
}
