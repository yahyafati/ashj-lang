package com.yahya.interpretor.ash.expr;

import com.yahya.interpretor.ash.Token;

public class Unary extends Expr {
    final Token operator;
    final Expr right;

    Unary(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

}
