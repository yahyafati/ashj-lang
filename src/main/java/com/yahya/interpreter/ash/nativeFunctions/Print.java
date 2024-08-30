package com.yahya.interpreter.ash.nativeFunctions;

import com.yahya.interpreter.ash.AshCallable;
import com.yahya.interpreter.ash.Interpreter;

import java.util.List;

public class Print implements AshCallable {
    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        System.out.println(Interpreter.stringify(arguments.get(0)));
        return null;
    }
}
