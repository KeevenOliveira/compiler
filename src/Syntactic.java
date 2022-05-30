import java.io.FileNotFoundException;

public class Syntactic {
    private Lexic lexic;
    private Token token;

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
            System.out.println("The code is working, analystic syntatic completed!");
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Your code does not working normal, near: " + this.token.getLexeme());
        }

    }

    // Case/Bloco
    private void B() throws FileNotFoundException {
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
        }
    }

    // Expression Relational
    private void ER() throws FileNotFoundException {
        if (!(this.token.getType() == Token.IDENTIFIER_TYPE ||
                this.token.getType() == Token.REAL_TYPE ||
                this.token.getType() == Token.INTEGER_TYPE)) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error inside condition of 'if' near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();

        if (this.token.getType() != Token.OPERATOR_RELATIONAL_TYPE) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error in operator regular near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();

        if (!(this.token.getType() == Token.IDENTIFIER_TYPE ||
                this.token.getType() == Token.REAL_TYPE ||
                this.token.getType() == Token.INTEGER_TYPE)) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error after operator regular of 'if' near: " + this.token.getLexeme());
        }
    }

    public void Declaration() throws FileNotFoundException {
        if (!(this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float") ||
                this.token.getLexeme().equals("char"))) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Variable wrong near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
        if (this.token.getType() != Token.IDENTIFIER_TYPE) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Expected one command near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
        if (!this.token.getLexeme().equalsIgnoreCase(";")) {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Variable wrong near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
    }

    private void Attribution() throws FileNotFoundException {
        if (this.token.getType() == Token.IDENTIFIER_TYPE) {
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
        if (this.token.getType() == Token.INTEGER_TYPE ||
                this.token.getType() == Token.REAL_TYPE ||
                this.token.getType() == Token.IDENTIFIER_TYPE) {
            this.token = this.lexic.getNextToken();
        } else {
            this.lexic.getColumnAndLine(this.token.getLexeme());
            throw new RuntimeException("Error in attribution near: " + this.token.getLexeme());
        }

    }
}
