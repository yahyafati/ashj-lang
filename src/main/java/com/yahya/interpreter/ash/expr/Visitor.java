package com.yahya.interpreter.ash.expr;

public interface Visitor<R> {
	R visitBinaryExpr(Binary expr);
	R visitUnaryExpr(Unary expr);
	R visitLiteralExpr(Literal expr);
	R visitGroupingExpr(Grouping expr);
}
