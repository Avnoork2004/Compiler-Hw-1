package org.example;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Scanner class that's lexical scanner for a
 * compiler. Reads input character by character and identifies tokens.
 *
 * Scanner uses PushbackReader to unread characters.
 * Whitespace is ignored, and the token buffer
 * stores lexeme of the current token.
 *
 * @author Avnoor Kaur
 */

public class Scanner {

    /** Enum of token types scanner can recognize */
    public enum TOKEN {
        SCANEOF, ID, INTLITERAL, VAR, WRITE, INIT, EQUALS, NOTEQUALS,
        IF, THEN, ENDIF, WHILE, DO, ENDWHILE, CALCULATE, PLUS
    }

    /** Member variable : List of reserved words */
    private List<String> reservedWords = Arrays.asList(
            "var", "write", "init", "if", "then", "endif",
            "while", "do", "endwhile", "calculate"
    );

    /** Member variable: PushbackReader for Input */
    private PushbackReader reader;

    /** Member variable: StringBuilder for token Buffer */
    private StringBuilder tokenBuffer;


    /**
     * Constructor: Scanner with PushbackReader.
     *
     * @param inputReader PushbackReader with input to scan
     */
    public Scanner(PushbackReader inputReader) {
        this.reader = inputReader;
        this.tokenBuffer = new StringBuilder();
    }

    /**
     * Constructs Scanner with Reader. Wraps reader
     * in PushbackReader
     *
     * @param input Reader containing input to scan
     */
    public Scanner(Reader input) {
        this(new PushbackReader(input));
    }

    /** Clears current token buffer */
    private void clearTokenBuffer() {
        tokenBuffer.setLength(0);
    }


    /**
     * Method: Scans and returns next token from input
     *
     * Scanner recognizes all tokens in the enum identifiers, int literals, reserved keywords,
     * operators (+, =, !=), and ignores whitespace. After scanning,  lexeme
     * of the token is stored in token buffer
     *
     * @return TOKEN type of the next token
     */
    public TOKEN scan() {
        clearTokenBuffer();

        try {
            int c;
            // Skips the whitespace
            do {
                c = reader.read();
                if (c == -1) return TOKEN.SCANEOF;
            } while (Character.isWhitespace(c));

            tokenBuffer.append((char) c);

            // Integer literal
            if (Character.isDigit(c)) {
                while (true) {
                    int next = reader.read();
                    if (next != -1 && Character.isDigit(next)) {
                        tokenBuffer.append((char) next);
                    } else {
                        if (next != -1) reader.unread(next);
                        break;
                    }
                }
                return TOKEN.INTLITERAL;
            }

            // Identifier or reserved words
            if (Character.isLetter(c)) {
                while (true) {
                    int next = reader.read();
                    if (next != -1 && Character.isLetterOrDigit(next)) {
                        tokenBuffer.append((char) next);
                    } else {
                        if (next != -1) reader.unread(next);
                        break;
                    }
                }
                String lexeme = tokenBuffer.toString();
                if (reservedWords.contains(lexeme)) {
                    switch (lexeme) {
                        case "var": return TOKEN.VAR;
                        case "write": return TOKEN.WRITE;
                        case "init": return TOKEN.INIT;
                        case "if": return TOKEN.IF;
                        case "then": return TOKEN.THEN;
                        case "endif": return TOKEN.ENDIF;
                        case "while": return TOKEN.WHILE;
                        case "do": return TOKEN.DO;
                        case "endwhile": return TOKEN.ENDWHILE;
                        case "calculate": return TOKEN.CALCULATE;
                    }
                }
                return TOKEN.ID;
            }

            // Operators
            switch (c) {
                case '+': return TOKEN.PLUS;
                case '=': return TOKEN.EQUALS;
                case '!':
                    int next = reader.read();
                    if (next == '=') {
                        tokenBuffer.append((char) next);
                        return TOKEN.NOTEQUALS;
                    } else {
                        if (next != -1) reader.unread(next);
                        throw new IOException("Unexpected char: !");
                    }
                default:
                    throw new IOException("Unexpected char: " + (char) c);
            }

        } catch (IOException e) {
            System.err.println("Scan error: " + e.getMessage());
            return TOKEN.SCANEOF;
        }
    }

    /**
     * Method: gets token buffer string, representing lexeme
     * of recently scanned token
     *
     * @return token buffer string
     */
    public String getTokenBufferString() {
        return tokenBuffer.toString();
    }



}
