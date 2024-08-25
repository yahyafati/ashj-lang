package com.yahya.interpreter.ash.stmt;


import com.yahya.interpreter.ash.Token;

public class Continue extends Stmt {

    public final Token token;

    public Continue(Token token) {
        this.token = token;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitContinueStmt(this);
    }
}
