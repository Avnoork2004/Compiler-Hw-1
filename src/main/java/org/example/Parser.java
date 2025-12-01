package org.example;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser class implements recursive descent parser for a simple language
 * supports conditionals, variable declarations, statements, loops, & arithmetic operations
 *
 * Parser uses a Scanner for lexical analysis and constructs an
 * AbsSynTree representing the Abstract Syntax Tree (AST) for the input program
 * It also maintains a symbol table for tracking declared variables and their types
 *
 * @author Avnoor Kaur
 */

public class Parser {

    /** AST constructed during parsing. */
    private AbsSynTree ast = new AbsSynTree();

    /**
     * @return AST after parsing
     *
     */
    public AbsSynTree getAst() {
        return ast;
    }


    /**
     * Enum representing data types
     */
    public enum TYPE {
        INTDATATYPE
    }

    /**
     * Symbol table entry containing variable's name & type
     */
    public static class SymbolTableItem {
        /** variable name */
        String name;
        /** data type of variable */
        TYPE type;

        /**
         * Constructs new {@code SymbolTableItem}.
         *
         * @param name of the variable
         * @param type of the variable
         */
        public SymbolTableItem(String name, TYPE type) {
            this.name = name;
            this.type = type;
        }
    }

    /** Symbol table storing variable names & SymbolTableItem objects */
    private Map<String, SymbolTableItem> symbolTable = new HashMap<>();

    /** Scanner to tokenize input program */
    private Scanner scanner;
    /** Next token to be processed in parsing sequence */
    private Scanner.TOKEN nextToken;

    /**
     * Matches expected token against next token from scanner
     *
     * If expected token matches, prints confirmation message, scans next token
     * Otherwise, throws parsing exception with details about mismatch
     *
     *
     * @param expected token type
     * @throws Exception if next token doesn't match expected token
     */
    private void match(Scanner.TOKEN expected) throws Exception {
        if (nextToken == expected) {
            System.out.println("Matched: " + nextToken + " Buffer: " + scanner.getTokenBufferString());
            nextToken = scanner.scan();
        } else {
            throw new Exception("Parse Error\nExpected: " + expected + "\nReceived: " +
                    nextToken + "\nBuffer: " + scanner.getTokenBufferString());
        }
    }

    /**
     * Parses program as string input
     *
     * Initializes Scanner for input, begins parsing using recursive descent,
     * verifies the end of file (EOF) token is reached
     *
     *
     * @param program source code as a string
     * @return {@code true} if parsing succeeds, {@code false} if parsing error
     */
    public boolean parse(String program) {
        scanner = new Scanner(new PushbackReader(new StringReader(program)));
        try {
            nextToken = scanner.scan();
            program();
            match(Scanner.TOKEN.SCANEOF);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }


    /**
     * @return program node representing AST root
     *
     * @throws Exception if parsing fails
     */
    private AbsSynTree.NodeProgram program() throws Exception {
        AbsSynTree.NodeVars nodeVars = vars();
        AbsSynTree.NodeStmts nodeStmts = stmts();
        AbsSynTree.NodeProgram programNode = ast.new NodeProgram(nodeVars, nodeStmts);
        ast.setNodeProgram(programNode);
        return programNode;
    }

    /**
     * @return a node representing all declared variables
     *
     * @throws Exception if parsing fails
     */
    private AbsSynTree.NodeVars vars() throws Exception {
        AbsSynTree.NodeVars nodeVars = ast.new NodeVars();
        while (nextToken == Scanner.TOKEN.VAR) {
            nodeVars.add(varDecl());
        }
        return nodeVars;
    }

    /**
     *
     * @return variable identifier node
     *
     * @throws Exception if declaration is invalid or variable is redeclared
     */
    private AbsSynTree.NodeId varDecl() throws Exception {
        match(Scanner.TOKEN.VAR);
        String varName = scanner.getTokenBufferString();
        if (symbolTable.containsKey(varName)) throw new Exception("Variable already declared: " + varName);
        symbolTable.put(varName, new SymbolTableItem(varName, TYPE.INTDATATYPE));
        match(Scanner.TOKEN.ID);
        return ast.new NodeId(varName);
    }

    /**
     * @return node containing all parsed statements
     *
     * @throws Exception if parsing fails
     */
    private AbsSynTree.NodeStmts stmts() throws Exception {
        AbsSynTree.NodeStmts nodeStmts = ast.new NodeStmts();
        while (nextToken == Scanner.TOKEN.WRITE || nextToken == Scanner.TOKEN.INIT ||
                nextToken == Scanner.TOKEN.IF || nextToken == Scanner.TOKEN.WHILE ||
                nextToken == Scanner.TOKEN.CALCULATE) {
            nodeStmts.add(stmt());
        }
        return nodeStmts;
    }

    /**
     *
     * Parses a single statement and constructs corresponding AST node
     *
     * @return AbsSynTree.NodeStmt representing parsed statement
     *
     * @throws Exception if unexpected token encountered
     */
    private AbsSynTree.NodeStmt stmt() throws Exception {
        switch (nextToken) {
            case WRITE:
                match(Scanner.TOKEN.WRITE);
                String writeVar = scanner.getTokenBufferString();
                match(Scanner.TOKEN.ID);
                return ast.new NodeWrite(ast.new NodeId(writeVar));
            case INIT:
                match(Scanner.TOKEN.INIT);
                String initVar = scanner.getTokenBufferString();
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                int value = Integer.parseInt(scanner.getTokenBufferString());
                match(Scanner.TOKEN.INTLITERAL);
                return ast.new NodeInit(ast.new NodeId(initVar), ast.new NodeIntLiteral(value));
            case CALCULATE:
                match(Scanner.TOKEN.CALCULATE);
                String calcVar = scanner.getTokenBufferString();
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                AbsSynTree.NodeExpr expr = add();
                return ast.new NodeCalculate(ast.new NodeId(calcVar), expr);
            case IF:
                match(Scanner.TOKEN.IF);
                AbsSynTree.NodeId leftIf = ast.new NodeId(scanner.getTokenBufferString());
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                AbsSynTree.NodeId rightIf = ast.new NodeId(scanner.getTokenBufferString());
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.THEN);
                AbsSynTree.NodeStmts ifBody = stmts();
                match(Scanner.TOKEN.ENDIF);
                return ast.new NodeIf(leftIf, rightIf, ifBody);
            case WHILE:
                match(Scanner.TOKEN.WHILE);
                AbsSynTree.NodeId leftWhile = ast.new NodeId(scanner.getTokenBufferString());
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.NOTEQUALS);
                AbsSynTree.NodeId rightWhile = ast.new NodeId(scanner.getTokenBufferString());
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.DO);
                AbsSynTree.NodeStmts whileBody = stmts();
                match(Scanner.TOKEN.ENDWHILE);
                return ast.new NodeWhile(leftWhile, rightWhile, whileBody);
            default:
                throw new Exception("Unexpected token: " + nextToken);
        }
    }

    /**
     * @return node representing addition operation
     *
     * @throws Exception if parsing fails
     */
    private AbsSynTree.NodeExpr add() throws Exception {
        AbsSynTree.NodeExpr left = value();
        while (nextToken == Scanner.TOKEN.PLUS) {
            match(Scanner.TOKEN.PLUS);
            AbsSynTree.NodeExpr right = value();
            left = ast.new NodePlus(left, right);
        }
        return left;
    }

    /**
     * <AddEnd> ::= plus <Value> <AddEnd> | ""
     *
     * @throws Exception if parsing fails
     */
    private void addEnd() throws Exception {
        while (nextToken == Scanner.TOKEN.PLUS) {
            match(Scanner.TOKEN.PLUS);
            value();
        }
    }

    /**
     * @return node representing parsed value
     *
     * @throws Exception if next token isn't identifier or int literal
     */
    private AbsSynTree.NodeExpr value() throws Exception {
        if (nextToken == Scanner.TOKEN.ID) {
            String name = scanner.getTokenBufferString();
            match(Scanner.TOKEN.ID);
            return ast.new NodeId(name);
        } else if (nextToken == Scanner.TOKEN.INTLITERAL) {
            int val = Integer.parseInt(scanner.getTokenBufferString());
            match(Scanner.TOKEN.INTLITERAL);
            return ast.new NodeIntLiteral(val);
        } else {
            throw new Exception("Expected ID or INTLITERAL, got: " + nextToken);
        }
    }
}
