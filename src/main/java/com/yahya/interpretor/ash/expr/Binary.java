package com.yahya.interpretor.ash.expr;

import com.yahya.interpretor.ash.Token;

public class Binary extends Expr {
    final Expr left;
    final Token operator;
    final Expr right;

    Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

}
