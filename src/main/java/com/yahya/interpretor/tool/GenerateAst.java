package com.yahya.interpretor.tool;

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
        String path = outputDir + "/" + baseName + ".java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package com.yahya.interpretor.ash.expr;");
            writer.println();
            writer.println("import com.yahya.interpretor.ash.Token;");
            writer.println();
            writer.println("public abstract class " + baseName + " {");
            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        types.forEach((className, fields) -> {
            defineType(outputDir, baseName, className, fields);
        });
    }

    private static void defineType(String outputDir, String baseName, String className, List<String> fieldList) {
        String path = outputDir + "/" + className + ".java";
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("package com.yahya.interpretor.ash.expr;");
            writer.println();
            writer.println("import com.yahya.interpretor.ash.Token;");
            writer.println();
            writer.println("public class " + className + " extends " + baseName + " {");
            for (String field : fieldList) {
                writer.println("    final " + field + ";");
            }
            writer.println();
            String fields = String.join(", ", fieldList);
            writer.println("    " + className + "(" + fields + ") {");
            for (String field : fieldList) {
                String name = field.split(" ")[1];
                writer.println("        this." + name + " = " + name + ";");
            }
            writer.println("    }");
            writer.println();
            writer.println("}");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
