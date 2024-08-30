package com.yahya.interpreter.ash.stmt;

public interface VisitorAdaptor<R> extends Visitor<R> {
    @Override
    default R visitExpressionStmt(Expression stmt) {
        return null;
    }

    @Override
    default R visitClassStmt(Class stmt) {
        return null;
    }

    @Override
    default R visitPrintStmt(Print stmt) {
        return null;
    }

    @Override
    default R visitVarStmt(Var stmt) {
        return null;
    }

    @Override
    default R visitBlockStmt(Block stmt) {
        return null;
    }

    @Override
    default R visitIfStmt(If stmt) {
        return null;
    }

    @Override
    default R visitWhileStmt(While stmt) {
        return null;
    }

    @Override
    default R visitContinueStmt(Continue stmt) {
        return null;
    }

    @Override
    default R visitBreakStmt(Break stmt) {
        return null;
    }

    @Override
    default R visitFunctionStmt(Function stmt) {
        return null;
    }

    @Override
    default R visitReturnStmt(Return stmt) {
        return null;
    }
}
