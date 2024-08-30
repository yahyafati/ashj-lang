package com.yahya.interpreter.tool;

import com.yahya.interpreter.ash.Token;
import com.yahya.interpreter.ash.TokenType;
import com.yahya.interpreter.ash.expr.*;

public class AstPrinter implements Visitor<String> {

    public static void main(String[] args) {
        Expr expr = new Binary(
                new Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Literal(123)
                ),
                new Token(TokenType.STAR, "*", null, 1),
                new Grouping(
                        new Binary(
                                new Literal(45.67),
                                new Token(TokenType.PLUS, "+", null, 1),
                                new Grouping(
                                        new Unary(
                                                new Token(TokenType.MINUS, "-", null, 1),
                                                new Literal(89.0)
                                        )
                                )
                        )
                )
        );

        System.out.println(new AstPrinter().print(expr));
    }

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        return expr.name.lexeme;
    }

    @Override
    public String visitAssignExpr(Assign expr) {
        return parenthesize("=", new Variable(expr.name), expr.value);
    }

    @Override
    public String visitLogicalExpr(Logical expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Call expr) {
        StringBuilder builder = new StringBuilder();
        builder.append(expr.callee.accept(this)).append("(");
        for (Expr argument : expr.arguments) {
            builder.append(argument.accept(this)).append(", ");
        }
        if (!expr.arguments.isEmpty()) {
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append(")");
        return builder.toString();
    }

    @Override
    public String visitGetExpr(Get expr) {
        return expr.object.accept(this) + "." + expr.name.lexeme;
    }

    @Override
    public String visitSetExpr(Set expr) {
        return expr.object.accept(this) + "." + expr.name.lexeme + " = " + expr.value.accept(this);
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }
}
