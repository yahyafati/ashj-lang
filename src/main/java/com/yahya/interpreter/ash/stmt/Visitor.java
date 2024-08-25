package com.yahya.interpreter.ash.stmt;

public interface Visitor<R> {
    R visitExpressionStmt(Expression stmt);

    R visitPrintStmt(Print stmt);
}
