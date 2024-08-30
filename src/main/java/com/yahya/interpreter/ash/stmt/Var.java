package com.yahya.interpreter.ash.stmt;

import com.yahya.interpreter.ash.Token;
import com.yahya.interpreter.ash.expr.Expr;

public class Var extends Stmt {
	public final Token name;
	public final Expr initializer;

	public Var(Token name, Expr initializer) {
		this.name = name;
		this.initializer = initializer;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitVarStmt(this);
	}
}
