public class Syntactic {
    private Lexic lexic;
    private Token token;

    public Syntactic(Lexic lexic) {
        this.lexic = lexic;
    }

    public void S() { // initial state
        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("main")) {
            throw new RuntimeException("You need declare 'main' in initial code");
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals("(")) {
            throw new RuntimeException("Does not exists open paratheses of main method");
        }

        this.token = this.lexic.getNextToken();
        if (!token.getLexeme().equals(")")) {
            throw new RuntimeException("Does not exists close paratheses of main method");
        }
        this.token = this.lexic.getNextToken();

        B();
        if (this.token.getType() == Token.END_CODE) {
            System.out.println("The code is working, analystic syntatic completed!");
        } else {
            throw new RuntimeException("Your code does not working normal");
        }

    }

    private void B() {
        if (!this.token.getLexeme().equals("{")) {
            throw new RuntimeException("Error: Expected open braces");
        }

        this.token = this.lexic.getNextToken();

        CS();

        if (!this.token.getLexeme().equals("}")) {
            throw new RuntimeException("Error: Expected close braces");
        }

        this.token = this.lexic.getNextToken();
    }

    private void CS() {
        if ((this.token.getType() == Token.IDENTIFIER_TYPE) ||
                this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float")) {
            C();
            CS();
        } else {

        }
    }

    private void C() {
        if (this.token.getType() == Token.IDENTIFIER_TYPE) {
            Attribution();
        } else if (this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float")) {
            Declaration();
        } else {
            throw new RuntimeException("Expected one command near: " + this.token.getLexeme());
        }
    }

    public void Declaration() {
        if (!(this.token.getLexeme().equals("int") ||
                this.token.getLexeme().equals("float"))) {
            throw new RuntimeException("Variable wrong near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
        if (this.token.getType() != Token.IDENTIFIER_TYPE) {
            throw new RuntimeException("Expected one command near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
        if (!this.token.getLexeme().equalsIgnoreCase(";")) {
            throw new RuntimeException("Variable wrong near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
    }

    private void Attribution() {
        if (this.token.getType() != Token.IDENTIFIER_TYPE) {
            throw new RuntimeException("Error in attribution near: " + this.token.getLexeme());
        }

        this.token = this.lexic.getNextToken();
        if (this.token.getType() != Token.OPERATOR_ASSIGNMENT_TYPE) {
            throw new RuntimeException("Error in attribution near: " + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
        E();
        if (!this.token.getLexeme().equals(";")) {
            throw new RuntimeException("Error in attribution near:" + this.token.getLexeme());
        }
        this.token = this.lexic.getNextToken();
    }

    private void E() {
        if (this.token.getType() == Token.INTEGER_TYPE ||
                this.token.getType() == Token.REAL_TYPE ||
                this.token.getType() == Token.IDENTIFIER_TYPE) {
            this.token = this.lexic.getNextToken();

            if (this.token.getType() != Token.OPERATOR_ARITHMETIC_TYPE) {
                throw new RuntimeException("Error while attribution one variable near:" + this.token.getLexeme());
            }
            this.token = this.lexic.getNextToken();

            if (this.token.getType() == Token.INTEGER_TYPE ||
                    this.token.getType() == Token.REAL_TYPE ||
                    this.token.getType() == Token.IDENTIFIER_TYPE) {
                this.token = this.lexic.getNextToken();
            } else {
                throw new RuntimeException("Error while attribution one variable near: " + this.token.getLexeme());
            }
        } else {
            throw new RuntimeException("Error in attribution near: " + this.token.getLexeme());
        }

    }
}
