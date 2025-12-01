package org.example;

/**
 * The Main class entry point for parser and AST
 *
 * It parses the program using Parser class and constructs an AST using AbsSynTree class
 *
 * Example Output:
 * The output displays a depth-first traversal of the AST, including all variables,
 * statements, and expressions.
 *
 * @author Avnoor Kaur
 *
 */

public class Main {

    /**
     * The main entry point for the program
     *
     * If parsing is successful, it prints "Parsing successful!" displays the AST
     * If parsing fails, it prints "Parsing failed." with error message
     *
     */

    public static void main(String[] args) {String program = """
            var a
            var b
            init a = 1
            init b = 5
            while a != b do
                calculate a = a + 1
                write a
                if a = b then
                    write a
                endif
            endwhile
        """;

        // Initializes parser
        Parser parser = new Parser();

        // Parses input program
        if (parser.parse(program)) {
            System.out.println("Parsing successful!");
            AbsSynTree ast = parser.getAst();

            System.out.println();
            System.out.println("Abstract Syntax Tree:");

            // Displays AST using depth first traversal
            ast.show();
        } else {
            System.out.println("Parsing failed.");
        }
    }
}