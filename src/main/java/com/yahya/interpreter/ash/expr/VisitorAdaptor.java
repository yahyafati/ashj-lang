package com.yahya.interpreter.ash.expr;

public interface VisitorAdaptor<R> extends Visitor<R> {

    @Override
    default R visitBinaryExpr(Binary expr) {
        return null;
    }

    @Override
    default R visitGroupingExpr(Grouping expr) {
        return null;
    }

    @Override
    default R visitLiteralExpr(Literal expr) {
        return null;
    }

    @Override
    default R visitUnaryExpr(Unary expr) {
        return null;
    }

    @Override
    default R visitVariableExpr(Variable expr) {
        return null;
    }

    @Override
    default R visitAssignExpr(Assign expr) {
        return null;
    }

    @Override
    default R visitLogicalExpr(Logical expr) {
        return null;
    }

    @Override
    default R visitCallExpr(Call expr) {
        return null;
    }
}
