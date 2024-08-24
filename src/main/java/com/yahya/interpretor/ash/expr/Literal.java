package com.yahya.interpretor.ash.expr;

public class Literal extends Expr {
    final Object value;

    Literal(Object value) {
        this.value = value;
    }

}
