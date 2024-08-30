package com.yahya.interpreter.ash.expr;

public interface Visitor<R> {
    R visitBinaryExpr(Binary expr);

    R visitGroupingExpr(Grouping expr);

    R visitLiteralExpr(Literal expr);

    R visitUnaryExpr(Unary expr);

    R visitVariableExpr(Variable expr);

    R visitAssignExpr(Assign expr);

    R visitLogicalExpr(Logical expr);

    R visitCallExpr(Call expr);

    R visitGetExpr(Get expr);

    R visitSetExpr(Set expr);

    R visitThisExpr(This expr);
}
