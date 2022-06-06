public class Token {
    public static int INTEGER_TYPE = 0; // HAS
    public static int REAL_TYPE = 1;// HAS
    public static int CHAR_TYPE = 2; // HAS
    public static int IDENTIFIER_TYPE = 4; // HAS
    public static int OPERATOR_RELATIONAL_TYPE = 5; // HAS
    public static int OPERATOR_ARITHMETIC_TYPE = 6; // HAS
    public static int OPERATOR_ASSIGNMENT_TYPE = 7;
    public static int CHARACTER_SPECIAL_TYPE = 8; // HAS
    public static int WORD_RESERVED_TYPE = 9; // HAS
    public static int END_CODE = 99; // HAS
    private int type; // return token
    private String lexeme; // token content

    public Token(String lexeme, int type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public int getType() {
        return this.type;
    }

    @Override
    public String toString() {
        switch (this.type) {
            case 0:
                return this.lexeme + " - INTEIRO";
            case 1:
                return this.lexeme + " - REAL";
            case 2:
                return this.lexeme + " - CHAR";
            case 3:
                return this.lexeme + " - CHAR E INTEIRO";
            case 4:
                return this.lexeme + " - IDENTIFICADOR";
            case 5:
                return this.lexeme + " - OPERADOR_RELACIONAL";
            case 6:
                return this.lexeme + " - OPERADOR_ARITMETICO";
            case 7:
                return this.lexeme + " - OPERADOR DE ATRIBUIÇÃO";
            case 8:
                return this.lexeme + " - CARACTER_ESPECIAL";
            case 9:
                return this.lexeme + " - PALAVRA_RESERVADA";
            case 99:
                return this.lexeme + " - FIM_CODIGO";
        }
        return "";
    }
}