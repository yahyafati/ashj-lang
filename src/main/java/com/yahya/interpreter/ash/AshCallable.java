package com.yahya.interpreter.ash;

import java.util.List;

public interface AshCallable {

    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}
