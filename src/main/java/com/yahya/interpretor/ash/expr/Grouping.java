package com.yahya.interpretor.ash.expr;

public class Grouping extends Expr {
    final Expr expression;

    Grouping(Expr expression) {
        this.expression = expression;
    }

}
