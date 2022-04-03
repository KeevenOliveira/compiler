
public class Token {
    public static int INTEGER_TYPE = 0; // HAS
    public static int REAL_TYPE = 1;// HAS
    public static int CHAR_TYPE = 2; // HASN'T
    public static int IDENTIFIER_TYPE = 3; // HAS
    public static int OPERATOR_RELATIONAL_TYPE = 4; // HASN'T
    public static int OPERATOR_ARITMETRIC_TYPE = 5; // HASN'T
    public static int CHARACTER_SPECIAL_TYPE = 6; // HAS
    public static int WORD_RESERVED_TYPE = 7; // HASN'T
    public static int END_CODE = 99; // HAS

    private int type; // tipo do token
    private String lexeme; // conteúdo do token

    public Token(String lexeme, int type) {
        this.lexeme = lexeme;
        this.type = type;
    }

    public String getLexema() {
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
                return this.lexeme + " - IDENTIFICADOR";
            case 4:
                return this.lexeme + " - OPERADOR_RELACIONAL";
            case 5:
                return this.lexeme + " - OPERADOR_ARITMETICO";
            case 6:
                return this.lexeme + " - CARACTER_ESPECIAL";
            case 7:
                return this.lexeme + " - PALAVRA_RESERVADA";
            case 99:
                return this.lexeme + " - FIM_CODIGO";
        }
        return "";
    }
}