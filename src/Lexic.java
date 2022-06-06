import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Stream;

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
            throw new RuntimeException("Erro: problema ao encontrar o arquivo");
        }
    }

    public void getColumnAndLine(String tokenError) throws FileNotFoundException {
        try (BufferedReader inputStream = new BufferedReader(new FileReader("src//example.txt"))) {
            Stream<String> read = inputStream.lines();
            Object[] test = read.toArray();
            for (int i = 0; i < test.length; i++) {
                int value = test[i].toString().indexOf(tokenError);
                if (value != -1) {
                    System.out.println("Posição: " + value);
                    System.out.println("Linha: " + i);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
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
                    } else if (character == '_') {
                        lexeme.append(character);
                        state = 1;
                    } else if (this.isLetter(character)) {
                        lexeme.append(character);
                        state = 2;
                    } else if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 6;
                    } else if (character == '\'' || character == '\"') {
                        this.back();
                        state = 3;
                    } else if (character == '=') {
                        lexeme.append(character);
                        state = 10;
                    } else if (character == '<' || character == '>' || character == '!') {
                        lexeme.append(character);
                        state = 11;
                    } else if (character == '+' || character == '/' || character == '*' || character == '-') {
                        lexeme.append(character);
                        state = 12;
                    } else if (character == ')' ||
                            character == '(' ||
                            character == '{' ||
                            character == '}' ||
                            character == ',' ||
                            character == ';') {
                        lexeme.append(character);
                        state = 9;
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
                        state = 2;
                    } else {
                        if ("if".contentEquals(lexeme.toString()) ||
                                "main".contentEquals(lexeme.toString()) ||
                                "else".contentEquals(lexeme.toString()) ||
                                "while".contentEquals(lexeme.toString()) ||
                                "do".contentEquals(lexeme.toString()) ||
                                "for".contentEquals(lexeme.toString()) ||
                                "int".contentEquals(lexeme.toString()) ||
                                "float".contentEquals(lexeme.toString()) ||
                                "char".contentEquals(lexeme.toString())) {
                            this.back();
                            return new Token(lexeme.toString(), Token.WORD_RESERVED_TYPE);
                        }

                        this.back();
                        return new Token(lexeme.toString(), Token.IDENTIFIER_TYPE);
                    }
                    break;
                case 2:
                    if (this.isLetter(character) || this.isDigit(character) || character == '_') {
                        lexeme.append(character);
                        state = 2;
                    } else {
                        if ("if".contentEquals(lexeme.toString()) ||
                                "main".contentEquals(lexeme.toString()) ||
                                "else".contentEquals(lexeme.toString()) ||
                                "while".contentEquals(lexeme.toString()) ||
                                "do".contentEquals(lexeme.toString()) ||
                                "for".contentEquals(lexeme.toString()) ||
                                "int".contentEquals(lexeme.toString()) ||
                                "float".contentEquals(lexeme.toString()) ||
                                "char".contentEquals(lexeme.toString())) {
                            this.back();
                            return new Token(lexeme.toString(), Token.WORD_RESERVED_TYPE);
                        }

                        this.back();
                        return new Token(lexeme.toString(), Token.IDENTIFIER_TYPE);
                    }
                    break;
                case 3:
                    if (character == '\'' || character == '\"') {
                        lexeme.append(character);
                        state = 4;
                    } else {
                        throw new RuntimeException(
                                "Erro: sequência de caractere inválido \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 4:
                    if (this.isDigit(character) || this.isLetter(character)) {
                        lexeme.append(character);
                        state = 5;
                    } else {
                        throw new RuntimeException("Erro: sequência de Char inválida \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 5:
                    if (character == '\'' || character == '\"') {
                        lexeme.append(character);
                        return new Token(lexeme.toString(), Token.CHAR_TYPE);
                    } else {
                        throw new RuntimeException("Erro: sequência de Char inválida \"" + lexeme.toString() + "\"");
                    }
                case 6:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 6;
                    } else if (character == '.') {
                        lexeme.append(character);
                        state = 7;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.REAL_TYPE);
                    }
                    break;
                case 7:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 6;
                    } else {
                        throw new RuntimeException("Erro: número float inválido \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 8:
                    if (this.isDigit(character)) {
                        lexeme.append(character);
                        state = 6;
                    } else {
                        this.back();
                        return new Token(lexeme.toString(), Token.REAL_TYPE);
                    }
                    break;
                case 9:
                    this.back();
                    return new Token(lexeme.toString(), Token.CHARACTER_SPECIAL_TYPE);
                case 10:
                    if (character == '=') {
                        lexeme.append(character);
                        if ("==".contentEquals(lexeme.toString())) {
                            state = 0;
                            return new Token(lexeme.toString(), Token.OPERATOR_RELATIONAL_TYPE);
                        } else {
                            state = 10;
                        }
                    } else if (character == ' ' || character == '\t' || character == '\n' || character == '\r') {
                        this.back();
                        return new Token(lexeme.toString(), Token.OPERATOR_ASSIGNMENT_TYPE);
                    } else {
                        lexeme.append(character);
                        throw new RuntimeException("Erro: operador incorreto \"" + lexeme.toString() + "\"");
                    }
                    break;
                case 11:
                    if (character == '=') {
                        lexeme.append(character);
                        if ("<=".contentEquals(lexeme.toString()) ||
                                ">=".contentEquals(lexeme.toString()) ||
                                "!=".contentEquals(lexeme.toString())) {
                            state = 0;
                            return new Token(lexeme.toString(), Token.OPERATOR_RELATIONAL_TYPE);
                        } else {
                            throw new RuntimeException(
                                    "Erro: operador relacional incorreto \"" + lexeme.toString() + "\"");
                        }
                    } else if ("<".contentEquals(lexeme.toString()) ||
                            ">".contentEquals(lexeme.toString()) ||
                            "<=".contentEquals(lexeme.toString()) ||
                            ">=".contentEquals(lexeme.toString()) ||
                            "!=".contentEquals(lexeme.toString())) {
                        this.back();
                        return new Token(lexeme.toString(), Token.OPERATOR_RELATIONAL_TYPE);
                    } else if (character == ' ' || character == '\t' || character == '\n' || character == '\r') {
                        this.back();
                        return new Token(lexeme.toString(), Token.OPERATOR_ASSIGNMENT_TYPE);
                    } else {
                        throw new RuntimeException("Erro: operador relacional incorreto \"" + lexeme.toString() + "\"");
                    }
                case 12:
                    this.back();
                    return new Token(lexeme.toString(), Token.OPERATOR_ARITHMETIC_TYPE);
                case 99:
                    return new Token(lexeme.toString(), Token.END_CODE);
            }
        }
        return token;
    }
}