package com.yahya.interpreter.ash.stmt;

import com.yahya.interpreter.ash.Token;

import java.util.List;

public class Class extends Stmt {
    public final Token name;
    public final List<Function> methods;

    public Class(Token name, List<Function> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitClassStmt(this);
    }
}
