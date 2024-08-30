package com.yahya.interpreter.ash.stmt;

import com.yahya.interpreter.ash.Token;

import java.util.List;

public class Function extends Stmt {
    public final Token name;
    public final List<Token> params;
    public final List<Stmt> body;

    public Function(Token name, List<Token> params, List<Stmt> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public static boolean isInitFunction(Function function) {
        return function.name.lexeme.equals("init");
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitFunctionStmt(this);
    }
}
