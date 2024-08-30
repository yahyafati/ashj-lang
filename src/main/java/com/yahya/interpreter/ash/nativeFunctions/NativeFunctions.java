package com.yahya.interpreter.ash.nativeFunctions;

import com.yahya.interpreter.ash.AshCallable;

import java.util.HashMap;

public class NativeFunctions {

    public final static HashMap<String, AshCallable> NATIVE_FUNCTIONS = new HashMap<>();

    static {
        NATIVE_FUNCTIONS.put("print", new Print());
        NATIVE_FUNCTIONS.put("clock", new Clock());
    }
}
