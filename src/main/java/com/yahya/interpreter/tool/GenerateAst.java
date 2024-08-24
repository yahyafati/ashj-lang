package com.yahya.interpreter.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GenerateAst {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64); // 64 is the exit code for command line usage error
        }
        String outputDir = args[0];
        String baseName = "Expr";
        defineAst(outputDir, baseName, Map.of(
                "Binary", List.of("Expr left", "Token operator", "Expr right"),
                "Grouping", List.of("Expr expression"),
                "Literal", List.of("Object value"),
                "Unary", List.of("Token operator", "Expr right")
        ));
    }

    private static void defineAst(String outputDir, String baseName, Map<String, List<String>> types) {
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            boolean response = outputDirFile.mkdirs();
            if (!response) {
                throw new RuntimeException("Can't create output directory!");
            }
        }
        String path = outputDir + "/" + baseName + ".java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package com.yahya.interpreter.ash.expr;");
            writer.println();
            writer.println("import com.yahya.interpreter.ash.Token;");
            writer.println();
            writer.println("public abstract class " + baseName + " {");

            writer.println();
            writer.println("\tpublic abstract <R> R accept(Visitor<R> visitor);");

            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        types.forEach((className, fields) -> {
            defineType(outputDir, baseName, className, fields);
            defineVisitor(outputDir, baseName, types);
        });
    }

    private static void defineType(String outputDir, String baseName, String className, List<String> fieldList) {
        String path = outputDir + "/" + className + ".java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package com.yahya.interpreter.ash.expr;");
            writer.println();
            writer.println("import com.yahya.interpreter.ash.Token;");
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

    private static void defineVisitor(String outputDir, String baseName, Map<String, List<String>> types) {
        String path = outputDir + "/Visitor.java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package com.yahya.interpreter.ash.expr;");
            writer.println();
            writer.println("public interface Visitor<R> {");
            for (String type : types.keySet()) {
                writer.println("\tR visit" + type + baseName + "(" + type + " " + baseName.toLowerCase() + ");");
            }
            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
