package com.yahya.interpreter.ash;

import java.util.HashMap;
import java.util.Map;

public class AshInstance {

    private final AshClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    public AshInstance(AshClass klass) {
        this.klass = klass;
    }

    public Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        AshFunction method = this.klass.findMethod(name.lexeme);
        if (method != null) return method.bind(this);

        throw new RuntimeError(name,
                "Undefined property '" + name.lexeme + "'.");
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
}
