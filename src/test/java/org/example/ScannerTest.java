package org.example;

import org.junit.jupiter.api.Test;

import java.io.PushbackReader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Scanner class
 *
 * Verifies scanner correctly recognizes reserved words, identifiers, integer literals,
 * operators, and if, while, calculate
 *
 * @author Avnoor Kaur
 */
class ScannerTest {

    /**
     * creates new Scanner instance for source code string
     *
     * @param code to scan
     * @return scanner initialized with input string
     */
    private Scanner createScanner(String code) {
        return new Scanner(new PushbackReader(new StringReader(code)));
    }

    /**
     * Tests scanning variable declaration with identifier
     * VAR, ID, SCANEOF
     */
    @Test
    void testVarScore123() {
        Scanner scanner = createScanner("var score123");

        assertEquals(Scanner.TOKEN.VAR, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("score123", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /**
     * Tests initialization of variable with int literal
     * INIT, ID, EQUALS, INTLITERAL, SCANEOF
     */
    @Test
    void testInitScoreEquals600() {
        Scanner scanner = createScanner("init score = 600");

        assertEquals(Scanner.TOKEN.INIT, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("score", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(Scanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals("600", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /**
     * Tests scanning calculation with identifiers and operators
     * CALCULATE, ID, EQUALS, ID, PLUS, ID, SCANEOF
     */
    @Test
    void testCalculateNewsalary() {
        Scanner scanner = createScanner("calculate newsalary = originalsalary + raise");

        assertEquals(Scanner.TOKEN.CALCULATE, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("newsalary", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("originalsalary", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.PLUS, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("raise", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /**
     * Tests scanning a write statement with identifier
     * WRITE, ID, SCANEOF
     */
    @Test
    void testWriteSalary() {
        Scanner scanner = createScanner("write salary");

        assertEquals(Scanner.TOKEN.WRITE, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("salary", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /**
     * Tests scanning  if statement with equality and endif
     * IF, ID, EQUALS, ID, THEN, ENDIF, SCANEOF
     */
    @Test
    void testIfXEqualsYThenEndif() {
        Scanner scanner = createScanner("if x = y then endif");

        assertEquals(Scanner.TOKEN.IF, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("x", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("y", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.THEN, scanner.scan());
        assertEquals(Scanner.TOKEN.ENDIF, scanner.scan());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /**
     * Tests scanning an if-then statement with write statement
     * IF, ID, EQUALS, ID, THEN, WRITE, ID, ENDIF, SCANEOF
     */
    @Test
    void testIfXEqualsYThenWriteXEndif() {
        Scanner scanner = createScanner("if x = y then write x endif");

        assertEquals(Scanner.TOKEN.IF, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("x", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("y", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.THEN, scanner.scan());
        assertEquals(Scanner.TOKEN.WRITE, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("x", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.ENDIF, scanner.scan());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /**
     * Tests scanning a while loop with inequality, calculation, and arithmetic.
     * WHILE, ID, NOTEQUALS, ID, DO, CALCULATE, ID, EQUALS,
     *      ID, PLUS, INTLITERAL, ENDWHILE, SCANEOF
     */
    @Test
    void testWhileXNotEqualsYDoCalculateXEqualsXPlus1Endwhile() {
        Scanner scanner = createScanner("while x != y do calculate x = x + 1 endwhile");

        assertEquals(Scanner.TOKEN.WHILE, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("x", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.NOTEQUALS, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("y", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.DO, scanner.scan());
        assertEquals(Scanner.TOKEN.CALCULATE, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("x", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.EQUALS, scanner.scan());
        assertEquals(Scanner.TOKEN.ID, scanner.scan());
        assertEquals("x", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.PLUS, scanner.scan());
        assertEquals(Scanner.TOKEN.INTLITERAL, scanner.scan());
        assertEquals("1", scanner.getTokenBufferString());
        assertEquals(Scanner.TOKEN.ENDWHILE, scanner.scan());
        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }

    /** Tests scanning an empty input program */
    @Test
    void testEmptyProgram() {
        Scanner scanner = createScanner("");

        assertEquals(Scanner.TOKEN.SCANEOF, scanner.scan());
    }
}