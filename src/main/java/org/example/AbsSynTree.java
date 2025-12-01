package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * The AbsSynTree class represents AST structure
 * used by parser to represent the syntactic structure of a program
 *
 *
 * Each node implements a show() method which recursively performs
 * a depth first traversal and prints the tree in a structured format
 *
 * @author Avnoor Kaur
 *
 */

public class AbsSynTree {

    /** Root node of AST */
    private NodeProgram nodeProgram;

    /**
     * @return root program node of the AST
     */
    public NodeProgram getNodeProgram() {
        return nodeProgram;
    }

    /**
     * Sets root program node of AST
     *
     * @param nodeProgram root program node
     */
    public void setNodeProgram(NodeProgram nodeProgram) {
        this.nodeProgram = nodeProgram;
    }

    /**
     * Shows full AST by printing all parts
     */
    public void show() {
        if (nodeProgram != null) nodeProgram.show();
    }

    /**
     * Base class for all AST nodes
     * Every node can show itself
     */
    public abstract class NodeBase {
        public abstract void show();
    }


    /**
     * Base class for expression nodes (math or variables)
     */
    public abstract class NodeExpr extends NodeBase {}

    /**
     * Base class for statement nodes (if, while, or write)
     */
    public abstract class NodeStmt extends NodeBase {}

    /**
     * Represents a variable in AST
     */
    public class NodeId extends NodeExpr {
        public String name;
        public NodeId(String name) { this.name = name; }
        @Override
        public void show() { System.out.println("AST id " + name); }
    }

    /**
     * Represents int value in AST
     */
    public class NodeIntLiteral extends NodeExpr {
        public int value;
        public NodeIntLiteral(int value) { this.value = value; }
        @Override
        public void show() { System.out.println("AST int literal " + value); }
    }

    /**
     * Represents an addition expression (left + right)
     */
    public class NodePlus extends NodeExpr {
        public NodeExpr left, right;
        public NodePlus(NodeExpr left, NodeExpr right) { this.left = left; this.right = right; }
        @Override
        public void show() {
            System.out.println("AST plus");
            System.out.print("LHS: ");
            left.show();
            System.out.print("RHS: ");
            right.show();
        }
    }

    /**
     * Represents "write" statement that prints a variable
     */
    public class NodeWrite extends NodeStmt {
        public NodeId id;
        public NodeWrite(NodeId id) { this.id = id; }
        @Override
        public void show() {
            System.out.println("AST write");
            id.show();

        }
    }

    /**
     * Represents variable initialization  init a = 1)
     */
    public class NodeInit extends NodeStmt {
        public NodeId id;
        public NodeIntLiteral value;
        public NodeInit(NodeId id, NodeIntLiteral value) { this.id = id; this.value = value; }
        @Override
        public void show() {
            System.out.println("AST init");
            id.show();
            value.show();

        }
    }


    /**
     * Represents calculation (calculate a = a + 1)
     */
    public class NodeCalculate extends NodeStmt {
        public NodeId id;
        public NodeExpr expr;
        public NodeCalculate(NodeId id, NodeExpr expr) { this.id = id; this.expr = expr; }
        @Override
        public void show() {
            System.out.println("AST calculate");
            id.show();
            expr.show();
        }
    }

    /**
     * Represents "if" statement with condition and body
     */
    public class NodeIf extends NodeStmt {
        public NodeId left, right;
        public NodeStmts body;
        public NodeIf(NodeId left, NodeId right, NodeStmts body) { this.left = left; this.right = right; this.body = body; }
        @Override
        public void show() {
            System.out.println("AST if");
            System.out.print("LHS: ");
            left.show();
            System.out.print("RHS: ");
            right.show();
            System.out.println("if body");
            body.showWithoutLabel();
            System.out.println("AST endif");


        }
    }

    /**
     * Represents "while" loop with condition and body
     */
    public class NodeWhile extends NodeStmt {
        public NodeId left, right;
        public NodeStmts body;
        public NodeWhile(NodeId left, NodeId right, NodeStmts body) { this.left = left; this.right = right; this.body = body; }
        @Override
        public void show() {
            System.out.println("AST while");
            System.out.print("LHS: ");
            left.show();
            System.out.print("RHS: ");
            right.show();
            System.out.println("while body");
            body.showWithoutLabel();
            System.out.println("AST endwhile");
            System.out.println();

        }
    }

    /**
     * Holds list of statements in the program
     */
    public class NodeStmts extends NodeBase {
        public List<NodeStmt> stmts = new ArrayList<>();

        /**
         * Adds a statement to the list
         * @param stmt to add
         */
        public void add(NodeStmt stmt) { stmts.add(stmt); }

        @Override
        public void show() {

            System.out.println("AST Statements");
            for (NodeStmt stmt : stmts) stmt.show();

        }

        /**
         * Shows all statements without printing "AST Statements" label
         */
        public void showWithoutLabel() {
            for (NodeStmt stmt : stmts) stmt.show();
        }
    }

    /**
     * Holds all variable declarations in program
     */
    public class NodeVars extends NodeBase {
        public List<NodeId> vars = new ArrayList<>();

        /**
         * Adds variable to the list
         * @param id the variable to add
         */
        public void add(NodeId id) { vars.add(id); }
        @Override
        public void show() {
            System.out.println("AST Variables");
            for (NodeId id : vars) id.show();

        }
    }

    /**
     * Represents main program node that contains variables and statements
     */
    public class NodeProgram extends NodeBase {
        public NodeVars vars;
        public NodeStmts stmts;
        public NodeProgram(NodeVars vars, NodeStmts stmts) { this.vars = vars; this.stmts = stmts; }
        @Override
        public void show() {

            vars.show();
            stmts.show();
        }
    }
}