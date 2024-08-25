package com.yahya.interpreter.ash;

import com.yahya.interpreter.Ash;
import com.yahya.interpreter.ash.expr.*;
import com.yahya.interpreter.ash.stmt.*;

import java.util.List;

public class Interpreter implements
        com.yahya.interpreter.ash.expr.Visitor<Object>,
        com.yahya.interpreter.ash.stmt.Visitor<Void> {

    private Environment environment = new Environment();

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        if (TokenType.STRICTLY_NUMBER_ONLY_OPERATORS.contains(expr.operator.type)) {
            checkNumberOperands(expr.operator, left, right);
        }

        return switch (expr.operator.type) {
            case GREATER -> (double) left > (double) right;
            case GREATER_EQUAL -> (double) left >= (double) right;
            case LESS -> (double) left < (double) right;
            case LESS_EQUAL -> (double) left <= (double) right;
            case BANG_EQUAL -> !isEqual(left, right);
            case EQUAL_EQUAL -> isEqual(left, right);
            case MINUS -> (double) left - (double) right;
            case SLASH -> {
                if ((double) right == 0) {
                    throw new RuntimeError(expr.operator, "Division by zero.");
                }
                yield (double) left / (double) right;
            }
            case STAR -> (double) left * (double) right;
            case PLUS -> {
                if (left instanceof Double && right instanceof Double) {
                    yield (double) left + (double) right;
                }
                if (left instanceof String || right instanceof String) {
                    yield stringify(left) + stringify(right);
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or at least one string.");
            }
            default -> null;
        };
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);

        return switch (expr.operator.type) {
            case MINUS -> {
                checkNumberOperand(expr.operator, right);
                yield -(double) right;
            }
            case BANG -> !isTruthy(right);
            default -> null;
        };
    }

    @Override
    public Object visitVariableExpr(Variable expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visitAssignExpr(Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Ash.runtimeError(error);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
            return stringifyDouble((Double) object);
        }
        return object.toString();
    }

    private String stringifyDouble(Double object) {
        String text = object.toString();
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVarStmt(Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitBlockStmt(Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStmt(If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }

    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }
}
