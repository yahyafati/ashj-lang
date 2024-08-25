package com.yahya.interpreter.ash;

import java.util.Set;

public enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,


    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,


    // Literals.
    IDENTIFIER, STRING, NUMBER,


    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,
    EOF;

    public static final Set<TokenType> STRICTLY_NUMBER_ONLY_OPERATORS = Set.of(
            MINUS, SLASH, STAR, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL
    );
}