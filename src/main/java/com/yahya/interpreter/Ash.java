package com.yahya.interpreter;

import com.yahya.interpreter.ash.*;
import com.yahya.interpreter.ash.stmt.Stmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Ash {

    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    public static void runtimeError(RuntimeError error) {
        System.out.println("[line " + error.token.line + "]: " + error.getMessage());
        hadRuntimeError = true;
    }

    private static void report(int line, String where, String message) {
        System.out.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        if (hadError) return;

        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        if (hadError) return;

        interpreter.interpret(statements);
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) System.exit(65); // 65 is the exit code for data format error
        if (hadRuntimeError) System.exit(70); // 70 is the exit code for internal software error
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        for (; ; ) {
            hadError = false; // TODO: This is a hack to reset the error state. Fix this.
            System.out.print(">> ");
            String line = reader.readLine();
            if (line == null || line.equals("/exit")) {
                System.out.println("Ciao!");
                break;
            }
            if (line.equals("/clear")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                continue;
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            run(line);
        }

        // Indicate an error in the exit code.
        if (hadError) System.exit(65); // 65 is the exit code for data format error
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64); // 64 is the exit code for command line usage error
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            System.out.println("Welcome to Ash!");
            System.out.println("Type /exit to exit the interpreter.");
            System.out.println("Type /clear to clear the screen.");
            System.out.println("-----------------------------");
            runPrompt();
        }
    }
}