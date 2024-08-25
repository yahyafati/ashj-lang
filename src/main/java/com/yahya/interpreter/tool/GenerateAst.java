package com.yahya.interpreter.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;

public class GenerateAst {

    private static final String PACKAGE_NAME = "com.yahya.interpreter.ash";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64); // 64 is the exit code for command line usage error
        }
        String outputDir = args[0];

        final String tokenPackage = PACKAGE_NAME + ".Token";
        final String exprPackage = PACKAGE_NAME + ".expr.Expr";
        defineAst(outputDir, "Expr", List.of(
                        new Type("Binary", List.of("Expr left", "Token operator", "Expr right"), List.of(PACKAGE_NAME + ".Token")),
                        new Type("Grouping", List.of("Expr expression"), List.of()),
                        new Type("Literal", List.of("Object value"), List.of()),
                        new Type("Unary", List.of("Token operator", "Expr right"), List.of(tokenPackage)),
                        new Type("Variable", List.of("Token name"), List.of(tokenPackage)),
                        new Type("Assign", List.of("Token name", "Expr value"), List.of(tokenPackage))
                ),
                List.of(tokenPackage)
        );
        defineAst(outputDir, "Stmt",
                List.of(
                        new Type("Expression", List.of("Expr expression"), List.of(exprPackage)),
                        new Type("Print", List.of("Expr expression"), List.of(exprPackage)),
                        new Type("Var", List.of("Token name", "Expr initializer"), List.of(tokenPackage, exprPackage)),
                        new Type("Block", List.of("List<Stmt> statements"), List.of("java.util.List"))
                ),
                List.of(tokenPackage, exprPackage)
        );
    }


    private static void defineAst(
            String outputDirContainer,
            String baseName,
            List<Type> types,
            List<String> imports
    ) {
        String outputDir = Path.of(outputDirContainer, baseName.toLowerCase()).toString();
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            boolean response = outputDirFile.mkdirs();
            if (!response) {
                throw new RuntimeException("Can't create output directory!");
            }
        }
        String path = outputDir + "/" + baseName + ".java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package " + PACKAGE_NAME + "." + baseName.toLowerCase() + ";");
            writer.println();
//            writer.println("import com.yahya.interpreter.ash.Token;");
            for (String importStatement : imports) {
                writer.println("import " + importStatement + ";");
            }
            writer.println();
            writer.println("public abstract class " + baseName + " {");

            writer.println();
            writer.println("\tpublic abstract <R> R accept(Visitor<R> visitor);");

            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        types.forEach(type -> {
            defineType(outputDir, baseName, type);
        });
        defineVisitor(outputDir, baseName, types);
    }

    private static void defineType(String outputDir, String baseName, Type type) {
        String className = type.name;
        List<String> fieldList = type.fields;
        String path = outputDir + "/" + className + ".java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package " + PACKAGE_NAME + "." + baseName.toLowerCase() + ";");
            writer.println();
            for (String importStatement : type.imports) {
                writer.println("import " + importStatement + ";");
            }
            writer.println();
            writer.println("public class " + className + " extends " + baseName + " {");
            for (String field : fieldList) {
                writer.println("\tpublic final " + field + ";");
            }
            writer.println();
            String fields = String.join(", ", fieldList);
            writer.println("\tpublic " + className + "(" + fields + ") {");
            for (String field : fieldList) {
                String name = field.split(" ")[1];
                writer.println("\t\tthis." + name + " = " + name + ";");
            }
            writer.println("\t}");
            writer.println();

            writer.println("\t@Override");
            writer.println("\tpublic <R> R accept(Visitor<R> visitor) {");
            writer.println("\t\treturn visitor.visit" + className + baseName + "(this);");
            writer.println("\t}");
            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void defineVisitor(String outputDir, String baseName, List<Type> types) {
        String path = outputDir + "/Visitor.java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package " + PACKAGE_NAME + "." + baseName.toLowerCase() + ";");
            writer.println();
            writer.println("public interface Visitor<R> {");
            for (Type type : types) {
                writer.println("\tR visit" + type.name + baseName + "(" + type.name + " " + baseName.toLowerCase() + ");");
            }
            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private record Type(String name, List<String> fields, List<String> imports) {
    }
}
