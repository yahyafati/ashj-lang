package com.yahya.interpreter.ash.expr;

import com.yahya.interpreter.ash.Token;

public abstract class Expr {

	public abstract <R> R accept(Visitor<R> visitor);
}
