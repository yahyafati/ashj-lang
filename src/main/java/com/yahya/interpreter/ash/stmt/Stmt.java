package com.yahya.interpreter.ash.stmt;


public abstract class Stmt {

    public abstract <R> R accept(Visitor<R> visitor);
}
