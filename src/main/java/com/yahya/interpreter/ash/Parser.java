package com.yahya.interpreter.ash;

import com.yahya.interpreter.Ash;
import com.yahya.interpreter.ash.expr.*;
import com.yahya.interpreter.ash.stmt.Class;
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

    private int loopDepth = 0;

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

    private Stmt declaration() {
        try {
            if (match(CLASS)) return classDeclaration();
            if (match(FUN)) return functionDeclaration("function");
            if (match(VAR)) return varDeclaration();

            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Class classDeclaration() {
        Token name = consume(IDENTIFIER, "Expect class name.");
        Variable superclass = null;
        if (match(LESS)) {
            consume(IDENTIFIER, "Expect superclass name");
            superclass = new Variable(previous());
        }
        consume(LEFT_BRACE, "Expect '{' before class body.");

        List<Function> methods = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            methods.add(functionDeclaration("method"));
        }

        consume(RIGHT_BRACE, "Expect '}' after class body.");
        return new Class(name, superclass, methods);
    }

    private Function functionDeclaration(String kind) {
        Token name = consume(IDENTIFIER, "Expect " + kind + " name.");
        consume(LEFT_PAREN, "Expect '(' after " + kind + " name.");
        List<Token> parameters = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                if (parameters.size() >= 255) {
                    error(peek(), "Can't have more than 255 parameters.");
                }
                parameters.add(consume(IDENTIFIER, "Expect parameter name."));
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN, "Expect ')' after parameters.");
        consume(LEFT_BRACE, "Expect '{' before " + kind + " body.");
        List<Stmt> body = block();
        return new Function(name, parameters, body);
    }

    private Var varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");
        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }
        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Var(name, initializer);
    }

    private Stmt statement() {
        if (match(IF)) return ifStatement();
        if (match(WHILE)) return whileStatement();
        if (match(FOR)) return forStatement();
        if (match(CONTINUE)) return continueStatement();
        if (match(BREAK)) return breakStatement();
        if (match(RETURN)) return returnStatement();

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
        increaseLoopDepth();
        Stmt body = statement();
        decreaseLoopDepth();
        return new While(condition, body);
    }

    private Stmt forStatement() {
        consume(LEFT_PAREN, "Expect '(' after 'for'.");
        Stmt initializer;
        if (match(SEMICOLON)) {
            initializer = null;
        } else if (match(VAR)) {
            initializer = varDeclaration();
        } else {
            initializer = expressionStatement();
        }

        Expr condition = null;
        if (!check(SEMICOLON)) {
            condition = expression();
        }
        consume(SEMICOLON, "Expect ';' after loop condition.");

        Expr increment = null;
        if (!check(RIGHT_PAREN)) {
            increment = expression();
        }
        consume(RIGHT_PAREN, "Expect ')' after for clauses.");

        Stmt body = statement();

        if (increment != null) {
            body = new Block(List.of(body, new Expression(increment)));
        }

        if (condition == null) condition = new Literal(true);
        body = new While(condition, body);

        if (initializer != null) {
            body = new Block(List.of(initializer, body));
        }

        return body;
    }

    private Stmt continueStatement() {
        Token previous = previous();
        if (isNotInLoop()) {
            throw error(previous, "Cannot use 'continue' outside of a loop.");
        }
        consume(SEMICOLON, "Expect ';' after 'continue'.");
        return new Continue(previous);
    }

    private Stmt breakStatement() {
        Token previous = previous();
        if (isNotInLoop()) {
            throw error(previous, "Cannot use 'break' outside of a loop.");
        }
        consume(SEMICOLON, "Expect ';' after 'break'.");
        return new Break(previous);
    }

    private Stmt returnStatement() {
        Token keyword = previous();
        Expr value = null;
        if (!check(SEMICOLON)) {
            value = expression();
        }
        consume(SEMICOLON, "Expect ';' after return value.");
        return new Return(keyword, value);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expect '}' after block.");
        return statements;
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
            } else if (expr instanceof Get get) {
                return new Set(get.object, get.name, value);
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
        return call();
    }

    private Expr call() {
        Expr expr = primary();
        while (true) {
            if (match(LEFT_PAREN)) {
                expr = finishCall(expr);
            } else if (match(DOT)) {
                Token name = consume(IDENTIFIER,
                        "Expect property name after '.'.");
                expr = new Get(expr, name);
            } else {
                break;
            }
        }
        return expr;
    }

    private Expr finishCall(Expr callee) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                if (arguments.size() >= 255) {
                    error(peek(), "Can't have more than 255 arguments.");
                }
                arguments.add(expression());
            } while (match(COMMA));
        }
        Token paren = consume(RIGHT_PAREN,
                "Expect ')' after arguments.");
        return new Call(callee, paren, arguments);
    }

    private Expr primary() {
        if (match(FALSE)) return new Literal(false);
        if (match(TRUE)) return new Literal(true);
        if (match(NIL)) return new Literal(null);
        if (match(NUMBER, STRING)) {
            return new Literal(previous().literal);
        }
        if (match(SUPER)) {
            Token keyword = previous();
            consume(DOT, "Expect '.' after 'super'.");
            Token method = consume(IDENTIFIER, "Expect superclass method name.");
            return new Super(keyword, method);
        }
        if (match(THIS)) {
            return new This(previous());
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
                case RETURN:
                    return;
            }
            advance();
        }
    }

    private void increaseLoopDepth() {
        loopDepth++;
    }

    private void decreaseLoopDepth() {
        loopDepth--;
    }

    private boolean isNotInLoop() {
        return loopDepth <= 0;
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