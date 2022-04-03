
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lexic {
    private char[] content;
    private int indexContent;

    public Lexic(String path) {
        try {
            String contentStr;
            contentStr = new String(Files.readAllBytes(Paths.get(path)));
            this.content = contentStr.toCharArray();
            this.indexContent = 0;
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    // return next chart
    private char nextChar() {
        return this.content[this.indexContent++];
    }

    // Verify if exists next char or if arrived to final of path
    private boolean hasNextChar() {
        return indexContent < this.content.length;
    }

    // Rewinds the index that see to "next char" in unity
    private void back() {
        this.indexContent--;
    }

    // identify if char is letter lowercase or uppercase
    private boolean isLetter(char c) {
        if ((c >= 'a') && (c <= 'z')) {
            return true;
        } else if ((c >= 'A') && (c <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    // identify if char is digit
    private boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }

    // method return next token valid or return message of error
    public Token getNextToken() {
        Token token = null;
        char character;
        int state = 0;

        StringBuffer lexeme = new StringBuffer();
        while (this.hasNextChar()) {
            character = this.nextChar();
            switch (state) {
                case 0:
                    // ASCII
                    if (character == ' ' || character == '\t' || character == '\n' || character == '\r') {
                        state = 0;
                    } else if (this.isLetter(character) || character == '_') {
                        lexeme.append(character);
                        state = 1;
                    } else if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 2;
                    } else if (character == ')' ||
                            character == '(' ||
                            character == '{' ||
                            character == '}' ||
                            character == ',' ||
                            character == ';') {
                        lexeme.append(character);
                        state = 5;
                    } else if (character == '$') {
                        lexeme.append(character);
                        state = 99;
                        this.back();
                    } else {
                        lexeme.append(character);
                        throw new RuntimeException("Erro: token inválido \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 1:
                    if (this.isLetter(character) || this.isDigit(character) || character == '_') {
                        lexeme.append(character);
                        state = 1;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.IDENTIFIER_TYPE);
                    }
                    break;
                case 2:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 2;
                    } else if (character == '.') {
                        lexeme.append(character);
                        state = 3;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.INTEGER_TYPE);
                    }
                    break;
                case 3:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 4;
                    } else {
                        throw new RuntimeException("Erro: número float inválido \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 4:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 4;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.REAL_TYPE);
                    }
                    break;
                case 5:
                    this.back();
                    return new Token(lexeme.toString(), Token.CHARACTER_SPECIAL_TYPE);
                case 99:
                    return new Token(lexeme.toString(), Token.END_CODE);
            }
        }
        return token;
    }
}
