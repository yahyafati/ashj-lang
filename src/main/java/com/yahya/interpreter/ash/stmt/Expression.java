package com.yahya.interpreter.ash.stmt;

import com.yahya.interpreter.ash.expr.Expr;

public class Expression extends Stmt {
    public final Expr expression;

    public Expression(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}
