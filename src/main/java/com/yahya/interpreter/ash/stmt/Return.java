package com.yahya.interpreter.ash.stmt;

import com.yahya.interpreter.ash.Token;
import com.yahya.interpreter.ash.expr.Expr;

public class Return extends Stmt {
    public final Token keyword;
    public final Expr value;

    public Return(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitReturnStmt(this);
    }
}
