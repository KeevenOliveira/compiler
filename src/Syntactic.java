import java.io.FileNotFoundException;

public class Syntactic {
    private Lexic lexic;
    private Token token;
    private CircularLinkedList semantic = new CircularLinkedList();
    private CircularListNode tokenAux;

    public Syntactic(Lexic lexic) {
        this.lexic = lexic;
    }

    public void S() throws FileNotFoundException { // initial state

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("int")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("You need declare 'int' in initial code near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("main")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("You need declare 'main' in initial code, near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("(")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException(
                    "Does not exists open paratheses of main method, near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals(")")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException(
                    "Does not exists close paratheses of main method, near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();

        B();
        if (this.token.getType() == Token.END_CODE) {
            System.out.println("The code is working, analystic syntatic and semantic completed!");
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Your code does not working normal, near: " + this.token.getLexeme());
        }
    }

    // Case/Bloco
    private void B() throws FileNotFoundException {
        semantic = new CircularLinkedList();

        if (!this.token.getLexeme().equals("{")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error: Expected open braces, near" + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();

        CM();

        if (!this.token.getLexeme().equals("}")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error: Expected close braces, near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
    }

    // Command
    private void CM() throws FileNotFoundException {
        if (this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float") ||
                this.token.getLexeme().equals("char")) {
            DEC();
            CM();
        } else if (this.token.getLexeme().equals("{") ||
                this.token.getType() == Token.IDENTIFIER_TYPE) {
            CB();
            CM();
        } else if (this.token.getLexeme().equals("if")) {
            PR();
        } else if (this.token.getLexeme().equals("while")) {
            RL();
        } else if (this.token.getLexeme().equals("}")) {
            return;
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error: Expected command, near: " + this.token.getLexeme());
        }
    }

    private void CB() throws FileNotFoundException {
        if (this.token.getLexeme().equals("{")) {
            B();
        } else if (this.token.getType() == Token.IDENTIFIER_TYPE) {
            Attribution();
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error in command basic near: " +
                    this.token.getLexeme());
        }
    }

    // Declaration
    private void DEC() throws FileNotFoundException {
        if (this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float") ||
                this.token.getLexeme().equals("char")) {
            Declaration();
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Expected one command near: " + this.token.getLexeme());
        }
    }

    // While // Repeat Loop
    private void RL() throws FileNotFoundException {
        if (!this.token.getLexeme().equals("while")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Word reserved wrong near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("(")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException(
                    "Does not exists open paratheses of 'while' operator near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();

        ER();

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals(")")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException(
                    "Does not exists close paratheses of 'while' operator near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();

        CM();
    }

    // Relational
    private void PR() throws FileNotFoundException {
        if (!this.token.getLexeme().equals("if")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Word reserved wrong near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("(")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException(
                    "Does not exists open paratheses of 'if' operator near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();

        ER();

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals(")")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException(
                    "Does not exists close paratheses of 'if' operator near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();

        CM();

        // Finished command if

        if (this.token.getLexeme().equals("else")) {
            this.token = this.lexic.getNextToken();
            CM();
        } else {
            return;
        }
    }

    // Expression Relational
    private void ER() throws FileNotFoundException {
        if (this.token.getType() == Token.IDENTIFIER_TYPE) {

            CircularListNode node = semantic.search(this.token.getLexeme());

            if (node == null) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error: Variable does not exists near: " + this.token.getLexeme());
            }

            this.token = this.lexic.getNextToken();

            if (this.token.getType() != Token.OPERATOR_RELATIONAL_TYPE) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error in operator regular near: " + this.token.getLexeme());
            }

            this.token = this.lexic.getNextToken();

            if (node.getType() == Token.CHAR_TYPE && this.token.getType() == Token.CHAR_TYPE) {
                this.token = this.lexic.getNextToken();
            } else if (node.getType() == Token.INTEGER_TYPE && this.token.getType() == Token.INTEGER_TYPE) {
                this.token = this.lexic.getNextToken();
            } else if (node.getType() == Token.REAL_TYPE && this.token.getType() == Token.REAL_TYPE) {
                this.token = this.lexic.getNextToken();
            } else {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error: Type of variables does not match near: " + this.token.getLexeme());
            }
            return;

        } else if (this.token.getType() == Token.REAL_TYPE ||
                this.token.getType() == Token.INTEGER_TYPE) {

            this.token = this.lexic.getNextToken();

            if (this.token.getType() != Token.OPERATOR_RELATIONAL_TYPE) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error in operator regular near: " + this.token.getLexeme());
            }

            this.token = this.lexic.getNextToken();

            if (this.token.getType() == Token.CHAR_TYPE) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error: Type of variables does not match near: " + this.token.getLexeme());
            } else if (this.token.getType() == Token.IDENTIFIER_TYPE) {
                CircularListNode node = semantic.search(this.token.getLexeme());

                if (node == null) {
                    this.lexic.getColumnAndLine(this.token.getLexeme());
                    throw new RuntimeException("Error: Variable does not exists near: " + this.token.getLexeme());
                }

                if (node.getType() == Token.CHAR_TYPE) {
                    this.lexic.getColumnAndLine(this.token.getLexeme());
                    throw new RuntimeException(
                            "Error: Type of variables does not match near: " + this.token.getLexeme());
                } else if (node.getType() == Token.INTEGER_TYPE) {
                    return;
                } else if (node.getType() == Token.REAL_TYPE) {
                    return;
                } else {
                    this.lexic.getColumnAndLine(this.token.getLexeme());
                    throw new RuntimeException(
                            "Error: Type of variables does not match near: " + this.token.getLexeme());
                }
            } else if (this.token.getType() == Token.REAL_TYPE ||
                    this.token.getType() == Token.INTEGER_TYPE) {
                return;
            }
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error in operator regular near: " + this.token.getLexeme());
        }

    }

    public void Declaration() throws FileNotFoundException {
        if (!(this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float") ||
                this.token.getLexeme().equals("char"))) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Variable wrong near: " + this.token.getLexeme());
        }
        int type = 0;
        if (this.token.getLexeme().equals("char")) {
            type = Token.CHAR_TYPE;
        } else if (this.token.getLexeme().equals("int")) {

            type = Token.INTEGER_TYPE;
        } else if (this.token.getLexeme().equals("float")) {
            type = Token.REAL_TYPE;
        }
        this.token = this.lexic.getNextToken();

        if (this.token.getType() != Token.IDENTIFIER_TYPE) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Expected one command near: " + this.token.getLexeme());
        }
        String variable = this.token.getLexeme();

        CircularListNode isDeclared = semantic.search(variable);

        if (isDeclared != null) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Variable already declared near: " + this.token.getLexeme());
        }

        semantic.addLast(type, variable);

        this.token = this.lexic.getNextToken();
        if (!this.token.getLexeme().equalsIgnoreCase(";")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Variable wrong near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
    }

    private void Attribution() throws FileNotFoundException {
        if (this.token.getType() == Token.IDENTIFIER_TYPE) {
            String variable = this.token.getLexeme();
            tokenAux = semantic.search(variable);
            if (tokenAux == null) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Variable not declared near: " + this.token.getLexeme());
            }

            this.token = this.lexic.getNextToken();

            if (!this.token.getLexeme().equals("=")) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error in operator regular near: " + this.token.getLexeme());
            }

            this.token = this.lexic.getNextToken();

            EXP();

            if (!this.token.getLexeme().equals(";")) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Error in operator regular near: " + this.token.getLexeme());
            }

            this.token = this.lexic.getNextToken();
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error in attribution near: " + this.token.getLexeme());
        }
    }

    private void EXP() throws FileNotFoundException {
        if (this.token.getType() == Token.CHAR_TYPE) {
            if (tokenAux.getType() != this.token.getType()) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Variable with type different, near: " + this.token.getLexeme());
            }
            this.token = this.lexic.getNextToken();
            EXP();
        } else if (this.token.getType() == Token.INTEGER_TYPE) {
            if (tokenAux.getType() != this.token.getType()) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Variable with type different, near: " + this.token.getLexeme());
            }
            this.token = this.lexic.getNextToken();
            if (this.token.getLexeme().equals("*") ||
                    this.token.getLexeme().equals("/") ||
                    this.token.getLexeme().equals("+") ||
                    this.token.getLexeme().equals("-")) {
                this.token = this.lexic.getNextToken();
                EXP();
            } else if (this.token.getLexeme().equals(";")) {
                return;
            }
        }
        if (this.token.getType() == Token.REAL_TYPE ||
                this.token.getType() == Token.IDENTIFIER_TYPE) {
            if (tokenAux.getType() != this.token.getType()) {
                this.lexic.getColumnAndLine(this.token.getLexeme());
                throw new RuntimeException("Variable with type different, near: " + this.token.getLexeme());
            }
            this.token = this.lexic.getNextToken();

            if (this.token.getLexeme().equals("*") ||
                    this.token.getLexeme().equals("/") ||
                    this.token.getLexeme().equals("+") ||
                    this.token.getLexeme().equals("-")) {
                this.token = this.lexic.getNextToken();
                EXP();
            } else if (this.token.getLexeme().equals(";")) {
                return;
            }
        } else if (this.token.getLexeme().equals(";")) {
            return;
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error in attribution near: " + this.token.getLexeme());
        }

    }
}
