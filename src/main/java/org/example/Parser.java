package org.example;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser class implements recursive descent parser for a simple language
 * supports conditionals, variable declarations, statements, loops, & arithmetic operations
 *
 * Parser uses a Scanner to tokenize input and a symbol table to store
 * variables & types
 *
 * @author Avnoor Kaur
 */

public class Parser {

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
     * <Program> ::= <Vars> <Stmts> $
     *
     * @throws Exception if parsing fails
     */
    private void program() throws Exception {
        vars();
        stmts();
    }

    /**
     * <Vars> ::= <Var> <Vars> | ""
     *
     * @throws Exception if parsing fails
     */
    private void vars() throws Exception {
        while (nextToken == Scanner.TOKEN.VAR) {
            varDecl();
        }
    }

    /**
     * <Var> ::= var id
     *
     *  Ensures declared variable isn't already in the symbol table
     *
     * @throws Exception if declaration is invalid or variable is redeclared
     */
    private void varDecl() throws Exception {
        match(Scanner.TOKEN.VAR);
        if (nextToken != Scanner.TOKEN.ID) {
            throw new Exception("Parse Error\nExpected: ID\nReceived: " +
                    nextToken + "\nBuffer: " + scanner.getTokenBufferString());
        }
        String varName = scanner.getTokenBufferString();
        if (symbolTable.containsKey(varName)) {
            throw new Exception("Parse Error: Variable already declared: " + varName);
        }
        symbolTable.put(varName, new SymbolTableItem(varName, TYPE.INTDATATYPE));
        match(Scanner.TOKEN.ID);
    }

    /**
     * <Stmts> ::= <Stmt> <Stmts> | ""
     *
     * @throws Exception if parsing fails
     */
    private void stmts() throws Exception {
        while (nextToken == Scanner.TOKEN.WRITE || nextToken == Scanner.TOKEN.INIT ||
                nextToken == Scanner.TOKEN.IF || nextToken == Scanner.TOKEN.WHILE ||
                nextToken == Scanner.TOKEN.CALCULATE) {
            stmt();
        }
    }

    /**
     * <Stmt> ::= ...
     *
     * Handles different types of statements
     * WRITE, INIT, IF, WHILE, & CALCULATE
     *
     *
     * @throws Exception if unexpected token encountered
     */
    private void stmt() throws Exception {
        switch (nextToken) {
            case WRITE:
                match(Scanner.TOKEN.WRITE);
                match(Scanner.TOKEN.ID);
                break;
            case INIT:
                match(Scanner.TOKEN.INIT);
                String initVar = scanner.getTokenBufferString();
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                match(Scanner.TOKEN.INTLITERAL);
                break;
            case IF:
                match(Scanner.TOKEN.IF);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.THEN);
                stmts();
                match(Scanner.TOKEN.ENDIF);
                break;
            case WHILE:
                match(Scanner.TOKEN.WHILE);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.NOTEQUALS);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.DO);
                stmts();
                match(Scanner.TOKEN.ENDWHILE);
                break;
            case CALCULATE:
                match(Scanner.TOKEN.CALCULATE);
                match(Scanner.TOKEN.ID);
                match(Scanner.TOKEN.EQUALS);
                add();
                break;
            default:
                throw new Exception("Parse Error: Unexpected token " + nextToken +
                        " Buffer: " + scanner.getTokenBufferString());
        }
    }

    /**
     * <Add> ::= <Value> <AddEnd>
     *
     * @throws Exception if parsing fails
     */
    private void add() throws Exception {
        value();
        addEnd();
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
     * <Value> ::= id | intliteral
     *
     * @throws Exception if next token isn't identifier or int literal
     */
    private void value() throws Exception {
        if (nextToken == Scanner.TOKEN.ID) {
            match(Scanner.TOKEN.ID);
        } else if (nextToken == Scanner.TOKEN.INTLITERAL) {
            match(Scanner.TOKEN.INTLITERAL);
        } else {
            throw new Exception("Parse Error: Expected ID or INTLITERAL\nReceived: " +
                    nextToken + "\nBuffer: " + scanner.getTokenBufferString());
        }
    }
}
