package com.yahya.interpreter.ash.stmt;

import com.yahya.interpreter.ash.Token;

public class Break extends Stmt {
    public final Token token;

    public Break(Token token) {
        this.token = token;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitBreakStmt(this);
    }
}
