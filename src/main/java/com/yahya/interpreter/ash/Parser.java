package com.yahya.interpreter.ash;

import com.yahya.interpreter.Ash;
import com.yahya.interpreter.ash.expr.*;
import com.yahya.interpreter.ash.stmt.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static com.yahya.interpreter.ash.TokenType.*;


public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new java.util.ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private Stmt statement() {
        if (match(IF)) return ifStatement();
        if (match(WHILE)) return whileStatement();
        if (match(PRINT)) return printStatement();
        if (match(LEFT_BRACE)) return new Block(block());

        return expressionStatement();
    }

    private Stmt ifStatement() {
        consume(LEFT_PAREN, "Expect '(' after 'if'.");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expect ')' after if condition.");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(ELSE)) {
            elseBranch = statement();
        }
        return new If(condition, thenBranch, elseBranch);
    }

    private Stmt whileStatement() {
        consume(LEFT_PAREN, "Expect '(' after 'while'.");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expect ')' after condition.");
        Stmt body = statement();
        return new While(condition, body);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
    }


    private Stmt declaration() {
        try {
            if (match(VAR)) return varDeclaration();

            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");
        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }
        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Var(name, initializer);
    }

    private Stmt printStatement() {
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Print(value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Expression(expr);
    }

    private Expr expression() {
        return assignment();
    }


    private Expr assignment() {
        Expr expr = or();
        if (match(EQUAL)) {
            Token equals = previous();
            Expr value = assignment();
            // Why aren't we checking if the left side is a variable,
            // before evaluating the right side?
            if (expr instanceof Variable) {
                Token name = ((Variable) expr).name;
                return new Assign(name, value);
            }

            /*
             *  We report an error if the left-hand side isn’t a valid assignment target, but we don’t throw it
             * because the parser isn’t in a confused state where we need to go into panic mode and synchronize.
             */
            error(equals, "Invalid assignment target."); // Not throwing the error on purpose!
        }
        return expr;
    }

    private Expr or() {
        Expr expr = and();
        while (match(OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new Logical(expr, operator, right);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = equality();
        while (match(AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new Logical(expr, operator, right);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(FALSE)) return new Literal(false);
        if (match(TRUE)) return new Literal(true);
        if (match(NIL)) return new Literal(null);
        if (match(NUMBER, STRING)) {
            return new Literal(previous().literal);
        }
        if (match(IDENTIFIER)) {
            return new Variable(previous());
        }
        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }


    @ConsumingFunction
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Ash.error(token, message);
        return new ParseError();
    }

    /**
     * Checks if the current token matches any of the given types.
     * If a match is found, <b>advances</b> to the next token.
     *
     * @param types the types to match
     * @return true if any of the types match, false otherwise
     */
    @ConsumingFunction
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    @ConsumingFunction
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;
            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }

    /**
     * Annotation to indicate that a function consumes tokens.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
    @interface ConsumingFunction {
    }

    private static class ParseError extends RuntimeException {
    }
}