package com.yahya.interpreter.ash.expr;

import com.yahya.interpreter.ash.Token;

public class Assign extends Expr {
	public final Token name;
	public final Expr value;

	public Assign(Token name, Expr value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitAssignExpr(this);
	}
}
