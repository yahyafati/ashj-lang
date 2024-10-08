package com.yahya.interpreter.ash.stmt;

public interface Visitor<R> {
    R visitExpressionStmt(Expression stmt);

    R visitPrintStmt(Print stmt);

    R visitVarStmt(Var stmt);

    R visitBlockStmt(Block stmt);

    R visitIfStmt(If stmt);

    R visitWhileStmt(While stmt);

    R visitContinueStmt(Continue stmt);

    R visitBreakStmt(Break stmt);

    R visitFunctionStmt(Function stmt);

    R visitReturnStmt(Return stmt);

    R visitClassStmt(Class stmt);
}
